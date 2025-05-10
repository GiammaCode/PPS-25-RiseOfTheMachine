package controller

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
  def apply(): GameState =
    val worldState = GameFactory.createGame()
    val view = CLIView
    val strategy = SmartHumanStrategy
    GameState(worldState, view,  strategy)

case class GameState(worldState: WorldState,
                     view: GameView,
                     humanStrategy: PlayerStrategy[HumanAction]):

  import model.util.States.State.State

  private def doPlayerAction(action: AiAction): State[GameState, Unit] =
    State { gs =>
      val currentWorldState = gs.worldState
      val result = currentWorldState.playerAI.executeAction(action, currentWorldState.worldMap)
      (gs.copy(worldState = currentWorldState.updatePlayer(result.getPlayer).updateMap(result.getCity)), ())

    }

  private def doHumanAction(): State[GameState, Unit] =
    State {gs =>
      val currentWorldState = gs.worldState
      val action = gs.humanStrategy.decideAction(currentWorldState)
      val result = currentWorldState.playerHuman.executeAction(action, currentWorldState.worldMap)

      val updatedState = gs.worldState
        .updateHuman(result.getPlayer)
        .updateMap(result.getCity)
      (gs.copy(worldState = updatedState), ())
    }

  private def renderTurn(): State[GameState, AiAction] = State { gs =>
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
  }

  def gameTurn(): State[GameState, Unit] =
    for
      action <- renderTurn()
      _ <- doPlayerAction(action)
      _ <- doHumanAction()
    yield ()


  /*
  def startGame() : Unit =
    //TODO: used to create a dedicated test
    val actions = List(SabotageAction(), InfectAction(), EvolveAction())
    val userChoice = view.getInputForAction(List("Sabotage", "Infect", "Evolve")) // ottieni l'input
    val aiActionResult = InputHandler.getActionFromChoice(userChoice, actions)

    aiActionResult match {
      case Right(aiAction) =>
        doPlayerAction(aiAction)
      case Left(error) =>
        view.renderActionMenu(List("Sabotage", "Infect", "Evolve"))
    }
  */
@main def tryController(): Unit =
  val game = GameController.apply()
  val (updatedGameState, _) = game.gameTurn().run(game)



