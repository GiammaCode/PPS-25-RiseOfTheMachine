package controller

import controller.InputHandler.CityContext
import controller.InputHandling.InputHandlingError
import model.map.WorldMapModule.{DeterministicMapModule, createWorldMap}
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy.*
import model.util.GameDifficulty.{AIStats, Difficulty, HumanStats, aiStats}
import model.strategy
import view.ViewModule.{CLIView, GameView}

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  def apply(gameMode: Int)(using Difficulty): GameState =
    import model.util.GameDifficulty.given

    val worldState = createWorldState(createWorldMap(10)(DeterministicMapModule), PlayerAI.fromStats, PlayerHuman.fromStats )
    val view = CLIView
    val strategy = SmartHumanStrategy
    GameState(worldState, view,  strategy, gameMode)

case class GameState(worldState: WorldState,
                     view: GameView,
                     humanStrategy: PlayerStrategy[HumanAction],
                     gameMode : Int): //TODO: it can be changed in a case class/enum

  import model.util.States.State.State

  private def getGameState: State[GameState, GameState] =
    State(gs => (gs, gs))

  private def doPlayerAction(action: AiAction): State[GameState, Unit] =
    State { gs =>
      val currentWorldState = gs.worldState
      val result = currentWorldState.playerAI.executeAction(action, currentWorldState.worldMap)
      (gs.copy(worldState = currentWorldState.updatePlayer(result.getPlayer).updateMap(result.getCity)), ())

    }

  private def doHumanAction(maybeAction: Option[HumanAction] = None): State[GameState, Unit] =
    State {gs =>
      val currentWorldState = gs.worldState
      val action = maybeAction.getOrElse(gs.humanStrategy.decideAction(currentWorldState))
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
    gameMode match
      case 1 =>
        for
          action <- renderTurn()
          _ <- doPlayerAction(action)
          _ <- doHumanAction()
        yield ()
      case 2 =>
        for
          gs <- getGameState
          aiInput = gs.view.renderAiPlayerTurn(gs.worldState)
          aiResult = InputHandler.getActionFromChoice(
            aiInput._1,
            CityContext(aiInput._2, gs.worldState.attackableCities.map(_._1)),
            gs.worldState.playerAI.getPossibleAction
          )
          _ <- aiResult match
            case Right(aiAction) => doPlayerAction(aiAction)
            case Left(_) =>
              println("Invalid AI input. Retrying...")
              gameTurn()

          // Now Human turn
          updatedGs <- getGameState
          humanInput = updatedGs.view.renderHumanPlayerTurn(updatedGs.worldState)
          humanResult = InputHandler.getActionFromChoice(
            humanInput._1,
            CityContext(humanInput._2, updatedGs.worldState.attackableCities.map(_._1)),
            updatedGs.worldState.playerHuman.getPossibleAction
          )
          _ <- humanResult match
            case Right(humanAction) => doHumanAction(Some(humanAction))
            case Left(_) =>
              println("Invalid Human input. Retrying...")
              gameTurn()
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
/*@main def tryController(): Unit =
  val game = GameController.apply()
  val (updatedGameState, _) = game.gameTurn().run(game)
*/


