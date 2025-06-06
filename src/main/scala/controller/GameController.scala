/**
 * == GameController ==
 * The main controller that manages the game loop, player (AI and human) actions,
 * the generation of the initial game state, and the turn-by-turn logic.
 *
 * == Main Types ==
 * - [[GameState]]: an opaque type representing the internal game state.
 *
 * == Main Functions ==
 * - [[buildGameState]]: initializes the game state.
 * - [[gameTurn]]: processes a full game turn.
 *
 * == Scope ==
 * This controller is designed for use with a CLI interface, but is abstract enough
 * to be reused in other UIs as well.
 */
package controller

import controller.InputHandler.CityContext
import controller.InputHandling.InputHandlingError
import model.map.WorldMapModule.{CreateModuleType, createWorldMap}
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy.*
import model.util.GameSettings.GameSettings

object GameController:

  /**
   * Concrete implementation of the opaque [[GameState]] type.
   * Encapsulates the game's world state.
   */
  case class GameStateImpl(worldState: WorldState)

  /**
   * Represents the result of a game turn,
   * including the AI action, success probability, and human action (if any).
   */
  private case class TurnResult(
                                 playerAction: AiAction,
                                 playerProb: Int,
                                 humanAction: Option[HumanAction]
                               )

  /**
   * Opaque type that represents the current state of the game.
   * Only exposes controlled operations via extension methods.
   */
  opaque type GameState = GameStateImpl

  private val startSizeOfTheMap = 8
  private val startingTurn = 0

  /**
   * Builds the initial game state using the given game settings and map module.
   *
   * @param settings the game settings, provided using the `using` clause
   * @param mapModule the module used to create the game map (e.g., deterministic or random)
   * @return a new [[GameState]]
   */
  def buildGameState(using settings: GameSettings, mapModule: CreateModuleType): GameState =
    GameStateImpl(
      createWorldState(
        createWorldMap(startSizeOfTheMap)(using mapModule),
        PlayerAI.fromStats,
        PlayerHuman.fromStats,
        startingTurn
      )
    )

  import model.util.States.State.State

  /**
   * Executes an AI action, updating the state if the probability check passes.
   *
   * @param action the AI action to execute
   * @param prob the success probability of the action
   * @return a state transformation
   */
  private[controller] def doPlayerAction(action: AiAction, prob: Int): State[GameState, Unit] =
    import model.util.Util.doesTheActionGoesRight
    if doesTheActionGoesRight(prob)
    then State(gs =>
      val currentWorldState = gs.worldState
      val actionResult = currentWorldState.playerAI.executeAction(action, currentWorldState.worldMap)
      val updatedWorldState = currentWorldState
        .updatePlayer(actionResult.getPlayer)
        .updateMap(actionResult.getCities)
      (gs.copy(worldState = updatedWorldState), ())
    )
    else State(gs => (gs, ()))

  /**
   * Executes the human player's action. If none is provided, a default strategy is used.
   *
   * @param maybeAction an optional explicit action; if none, a strategy will decide
   * @return a state transformation
   */
  private[controller] def doHumanAction(maybeAction: Option[HumanAction]): State[GameState, Unit] =
    State(gs =>
      val currentWorldState = gs.worldState
      val action = maybeAction.getOrElse(currentWorldState.playerHuman.decideActionByStrategy(currentWorldState))
      val actionResult = currentWorldState.playerHuman.executeAction(action, currentWorldState.worldMap)
      val updatedState = currentWorldState
        .updateHuman(actionResult.getPlayer)
        .updateMap(actionResult.getCities)
      (gs.copy(worldState = updatedState), ())
    )

  /**
   * Renders a turn and processes user input to determine actions.
   *
   * @return a [[TurnResult]] containing the resolved actions for the turn
   */
  private def renderTurn(using GameSettings): State[GameState, TurnResult] = State { gs =>
    val currentWorldState = gs.worldState.updateTurn
    import view.ViewModule.CLIView
    val turnInput = CLIView.renderGameTurn(currentWorldState)

    def resolveAction[A <: TurnAction](index: Int, city: String, options: List[A])(using InputHandler.ActionResolver[A]) =
      InputHandler.getActionFromChoice(index, CityContext(city, currentWorldState.attackableCities.map(_._1)), options)

    val playerResult = resolveAction(turnInput.aiInput_action, turnInput.aiInput_city, currentWorldState.playerAI.getPossibleAction)
    val humanResultOpt = turnInput.humanInput.map(resolveAction(_, _, currentWorldState.playerHuman.getPossibleAction))

    (playerResult, humanResultOpt) match
      case (Right(playerAction), humanOpt) if humanOpt.forall(_.isRight) =>
        val probability = currentWorldState.probabilityByCityandAction(turnInput.aiInput_city, playerAction)
        val humanActionOpt = humanOpt.flatMap(_.toOption)
        (gs.copy(worldState = currentWorldState), TurnResult(playerAction, probability, humanActionOpt))
      case _ =>
        println("Invalid input. Retrying turn.")
        renderTurn.run(gs)
  }

  /**
   * Executes a full game turn by processing the AI and human actions.
   *
   * @return a transformation that updates the game state
   */
  def gameTurn(using GameSettings): State[GameState, Unit] =
    for
      turn <- renderTurn
      _ <- doPlayerAction(turn.playerAction, turn.playerProb)
      _ <- doHumanAction(turn.humanAction)
    yield ()

  /**
   * Extension methods for the opaque [[GameState]] type.
   */
  extension (gs: GameState)

    /**
     * Retrieves the current world state.
     *
     * @return the internal [[WorldState]]
     */
    def worldState: WorldState = gs.worldState

    /**
     * Checks if the game is over and renders the endgame screen if so.
     *
     * @return true if the game is over, false otherwise
     */
    def isGameOver: Boolean =
      val (gameOver, winner) = worldState.isGameOver
      import view.ViewModule.CLIView
      if gameOver then CLIView.renderEndGame(winner.get)
      gameOver
