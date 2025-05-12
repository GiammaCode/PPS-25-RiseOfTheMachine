package controller

import controller.InputHandler.CityContext
import controller.InputHandling.InputHandlingError
import model.map.WorldMapModule.{DeterministicMapModule, createWorldMap}
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy
import model.strategy.*
import model.util.GameDifficulty.{Difficulty, aiStats}
import view.ViewModule.{CLIView, GameView}

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */

  import model.util.GameDifficulty.given

  def buildGameState(using Difficulty): GameState =
    GameState(
      createWorldState(
        createWorldMap(10)(DeterministicMapModule),
        PlayerAI.fromStats,
        PlayerHuman.fromStats),
      CLIView,
      SmartHumanStrategy
    )

case class GameState(worldState: WorldState,
                     view: GameView,
                     humanStrategy: PlayerStrategy[HumanAction]):

  import model.util.States.State.State

  private def doPlayerAction(action: AiAction): State[GameState, Unit] = State ( gs =>
      val currentWorldState = gs.worldState
      val result = currentWorldState.playerAI.executeAction(action, currentWorldState.worldMap)
      (gs.copy(worldState = currentWorldState.updatePlayer(result.getPlayer).updateMap(result.getCity)), ())
  )

  private def doHumanAction(): State[GameState, Unit] = State (gs =>
      val currentWorldState = gs.worldState
      val action = gs.humanStrategy.decideAction(currentWorldState)
      val result = currentWorldState.playerHuman.executeAction(action, currentWorldState.worldMap)

      val updatedState = gs.worldState
        .updateHuman(result.getPlayer)
        .updateMap(result.getCity)
      (gs.copy(worldState = updatedState), ())
  )

  private def renderTurn(): State[GameState, AiAction] = State ( gs =>
    val currentWorldState = gs.worldState
    val input = view.renderGameTurn(currentWorldState)
    val result = InputHandler.getActionFromChoice(
      input._1,
      CityContext(input._2,  currentWorldState.attackableCities.map(_._1)),
      currentWorldState.playerAI.getPossibleAction
    )
    result match
      case Right(action) => (gs, action)
      case Left(_) => renderTurn().run(gs)
  )

  def gameTurn(): State[GameState, Unit] =
    for
      action <- renderTurn()
      _ <- doPlayerAction(action)
      _ <- doHumanAction()
    yield ()


