package controller

import controller.InputHandler.CityContext
import controller.InputHandling.InputHandlingError
import model.map.WorldMapModule.createWorldMap
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy
import model.strategy.*
import model.util.GameSettings.GameSettings
import model.util.Util.doesTheActionGoesRight
import view.ViewModule.CLIView

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  case class GameStateImpl(worldState: WorldState,
                           humanStrategy: PlayerStrategy[HumanAction])

  private case class TurnResult(playerAction: AiAction,
                                 playerProb: Int,
                                 humanAction: Option[HumanAction])

  opaque type GameState = GameStateImpl

  import model.map.WorldMapModule.given
  import model.util.GameSettings.given
  given GameSettings = CLIView.renderGameModeMenu()

  def buildGameState(): GameState =
    GameStateImpl(
      createWorldState(createWorldMap(10),
      PlayerAI.fromStats,
      PlayerHuman.fromStats, 0),
      SmartHumanStrategy)

  import model.util.States.State.State
  private def getGameState: State[GameState, GameState] =
    State(gs => (gs, gs))

  private def doPlayerAction(action: AiAction, prob: Int): State[GameState, Unit] = if doesTheActionGoesRight(prob)
    then State ( gs =>
        val currentWorldState = gs.worldState
        val result = currentWorldState.playerAI.executeAction(action, currentWorldState.worldMap)
        (gs.copy(worldState = currentWorldState.updatePlayer(result.getPlayer).updateMap(result.getCity)), ())
        )
    else State(gs => (gs,()))

  private def doHumanAction(maybeAction: Option[HumanAction]): State[GameState, Unit] = State (gs =>
    val currentWorldState = gs.worldState
    val action = maybeAction.getOrElse(gs.humanStrategy.decideAction(currentWorldState))
    val result = currentWorldState.playerHuman.executeAction(action, currentWorldState.worldMap)
    val updatedState = gs.worldState.updateHuman(result.getPlayer).updateMap(result.getCity)
    (gs.copy(worldState = updatedState), ())
  )


  private def renderTurn(): State[GameState, TurnResult] = State ( gs =>
    val currentWorldState = gs.worldState.updateTurn
    val ((aiChoiceIndex, aiTargetCity), humanInputOpt) = CLIView.renderGameTurn(currentWorldState)

    val context = CityContext(aiTargetCity, currentWorldState.attackableCities.map(_._1))

    val playerResult = InputHandler.getActionFromChoice(
      aiChoiceIndex,
      context,
      currentWorldState.playerAI.getPossibleAction)

    val humanResultOpt = humanInputOpt.map(x =>
      InputHandler.getActionFromChoice(
        x._1,
        CityContext(x._2, currentWorldState.attackableCities.map(_._1)),
        currentWorldState.playerHuman.getPossibleAction))

    (playerResult, humanResultOpt) match
      case (Right(playerAction), Some(Right(humanAction))) =>
        val prob = currentWorldState.probabilityByCityandAction(aiTargetCity, playerAction)
        (gs.copy(worldState = currentWorldState), TurnResult(playerAction, prob, Some(humanAction)))

      case (Right(playerAction), None) =>
        val prob = currentWorldState.probabilityByCityandAction(aiTargetCity, playerAction)
        (gs.copy(worldState = currentWorldState), TurnResult(playerAction, prob, None))

      case _ =>
        println("Invalid input. Retrying turn.")
        renderTurn().run(gs))
  def gameTurn(): State[GameState, Unit] =
    for
      turn <- renderTurn()
      _ <- doPlayerAction(turn.playerAction,turn.playerProb)
      _ <- doHumanAction(turn.humanAction)
    yield ()

  extension(gs: GameState)
      def worldState: WorldState = gs.worldState

      def isGameOver: Boolean =
        worldState.isGameOver
        //chiamate view




