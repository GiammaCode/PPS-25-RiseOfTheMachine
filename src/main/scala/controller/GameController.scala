package controller

import controller.InputHandler.CityContext
import controller.InputHandling.InputHandlingError
import model.map.WorldMapModule.createWorldMap
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy
import model.strategy.*
import model.util.GameDifficulty.Difficulty
import model.util.GameMode.GameMode
import model.util.GameMode.GameMode.{Multiplayer, Singleplayer}
import view.ViewModule.CLIView

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  case class GameStateImpl(worldState: WorldState,
                           humanStrategy: PlayerStrategy[HumanAction])

  import model.map.WorldMapModule.given
  import model.util.GameDifficulty.given
  import model.util.GameMode.given

  opaque type GameState = GameStateImpl

  def buildGameState(using Difficulty ,GameMode): GameState =
    GameStateImpl(
      createWorldState(createWorldMap(10), PlayerAI.fromStats, PlayerHuman.fromStats),
      SmartHumanStrategy
    )

  import model.util.States.State.State
  private def getGameState: State[GameState, GameState] =
    State(gs => (gs, gs))


  private def doPlayerAction(action: AiAction): State[GameState, Unit] = State ( gs =>
    val currentWorldState = gs.worldState
    val result = currentWorldState.playerAI.executeAction(action, currentWorldState.worldMap)
    (gs.copy(worldState = currentWorldState.updatePlayer(result.getPlayer).updateMap(result.getCity)), ())
  )

  private def doHumanAction(maybeAction: Option[HumanAction]): State[GameState, Unit] = State (gs =>
    val currentWorldState = gs.worldState
    val action = maybeAction.getOrElse(gs.humanStrategy.decideAction(currentWorldState))
    val result = currentWorldState.playerHuman.executeAction(action, currentWorldState.worldMap)
    val updatedState = gs.worldState
      .updateHuman(result.getPlayer)
      .updateMap(result.getCity)
    (gs.copy(worldState = updatedState), ())
  )


  private def renderTurn(): State[GameState, (AiAction, HumanAction)] = State ( gs =>
    val currentWorldState = gs.worldState

    val input = CLIView.renderGameTurn(currentWorldState)(GameMode.Multiplayer)

    val playerResult = InputHandler.getActionFromChoice(
      input._1._1,
      CityContext(input._1._2, currentWorldState.attackableCities.map(_._1)),
      currentWorldState.playerAI.getPossibleAction
    )

    val humanResult = InputHandler.getActionFromChoice(
      input._2.get._1,
      CityContext(input._2.get._2, currentWorldState.attackableCities.map(_._1)),
      currentWorldState.playerHuman.getPossibleAction
    )

    (playerResult, humanResult) match
      case (Right(playerAction), Right(humanAction)) =>
        (gs, (playerAction, humanAction))

      case _ => renderTurn().run(gs))


  def gameTurn(): State[GameState, Unit] =
        for
          (playerAction,humanAction) <- renderTurn()
          _ <- doPlayerAction(playerAction)
          _ <- doHumanAction(Option(humanAction))
        yield ()


