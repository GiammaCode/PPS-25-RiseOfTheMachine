package controller

import controller.InputHandling.InputHandlingError
import model.map.WorldMapModule.WorldMap
import model.strategy.*
import model.strategy.PlayerAI.PlayerAI
import model.strategy.PlayerHuman.PlayerHuman
import model.strategy.playerActions.*
import view.ViewModule.{CLIView, GameView}

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  def apply(): GameState =
    val (ai, human, worldMap) = model.GameFactory.createGame()
    val view = CLIView
    GameState(ai, human, worldMap,view)

case class GameState(ai: PlayerAI,
                     human: PlayerHuman,
                     worldMap: WorldMap,
                      view: GameView):

  private val currentAi : PlayerAI = ai
  private val currentHuman : PlayerHuman = human
  private var currentMap : WorldMap = worldMap


  import model.util.States.State.State

  private def doPlayerAction(action: AiAction): State[GameState, Unit] =
    State { gs =>(gs.copy(ai = gs.currentAi.executeAction(action)), ())}

  private def doHumanAction(action: HumanAction): State[GameState, Unit] =
    State { gs => (gs.copy(human = gs.currentHuman.executeAction(action)),())}

  private def renderTurn(): State[GameState,Unit] =
    val abilities: Set[String] = Set.empty
    val options = List("SabotageAction", "InfectAction", "EvolveAction","Exit")
    State { state => view.renderGameTurn(3, worldMap, worldMap.numberOfCityInfected(), 13, abilities, options)
      (state, ())
    }

  def gameTurn(aiActionResult: Either[InputHandlingError, AiAction]): State[GameState, Unit] =
    aiActionResult match {
      case Right(aiAction) =>
        for
          _ <- renderTurn()
          _ <- doPlayerAction(aiAction)
        // _ <- doHumanAction(humanAction)
        yield()
      case Left(error) => ???
    }


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
  val actions = List(SabotageAction(), InfectAction(), EvolveAction()) //ai.getPossibleActions
  val aiActionResult = InputHandler.getActionFromChoice(3, actions)
  game.gameTurn(aiActionResult)(game)
