package controller

import controller.GameController.initialGameState
import controller.InputHandler.CityContext
import controller.InputHandling.InputHandlingError
import model.map.WorldState.WorldState
import model.strategy.*
import model.{GameFactory, strategy}
import view.ViewModule.{CLIView, GameView}

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  val initialGameState: GameState =
    GameState(
      worldState = GameFactory.createGame(),
      view = CLIView
    )

case class GameState(worldState: WorldState,
                     view: GameView):

  import model.util.States.State.State

  private def doPlayerAction(action: AiAction): State[GameState, Unit] = State ( gs =>
    val currentWorldState = gs.worldState
    val result = currentWorldState.playerAI.executeAction(action, currentWorldState.worldMap)
    (gs.copy(worldState = currentWorldState.updatePlayer(result.getPlayer).updateMap(result.getCity)), ())
  )

  private def doHumanAction(): State[GameState, Unit] = State (gs =>
    val currentWorldState = gs.worldState
    val action = CityDefense(List("i"))
    val result = currentWorldState.playerHuman.executeAction(action, currentWorldState.worldMap)
    (gs.copy(worldState = currentWorldState.updateHuman(result.getPlayer).updateMap(result.getCity)), ())
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


@main def startGame(): Unit =
  def loop(state: GameState): Unit =
    val (nextState, _) = state.gameTurn().run(state)
    loop(nextState)

  loop(initialGameState)


