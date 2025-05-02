package controller

import controller.InputHandling.InputHandlingError
import model.map.WorldMapModule.WorldMap
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy.*
import model.strategy.PlayerAI
import model.strategy
import model.util.States.State.State
import view.ViewModule.{CLIView, GameView}

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  def apply(): GameState =
    val worldState = model.GameFactory.createGame()
    val view = CLIView
    GameState(worldState,view)

case class GameState(worldState: WorldState,
                      view: GameView):



  import model.util.States.State.State

  private def doPlayerAction(action: AiAction): State[GameState, Unit] =
    State { gs =>
      val result = worldState.playerAI.executeAction(action,worldState.worldMap)
      val updatedAi = result.getPlayer
      val maybeCity = result.getCity
      (gs.copy(worldState= worldState.updatePlayer(updatedAi)), ())
   }

  private def doHumanAction(action: HumanAction): State[GameState, Unit] = ???
   // State { gs => (gs.copy(human = gs.getCurrentHuman.executeAction(action)),())}

  private def renderTurn(): State[GameState,Unit] =
    val abilities: Set[String] = Set.empty
    val options = List("SabotageAction", "InfectAction", "EvolveAction","Exit")
    State { state => view.renderGameTurn(worldState)
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

//@main def tryController(): Unit =
//  val game = GameController.apply()
//  val actions = List(SabotageAction(), InfectAction(), EvolveAction()) //ai.getPossibleActions
//  val attackableCities = Set("Rome", "Paris", "Berlin")
//  val aiActionResult = InputHandler.getActionFromChoice(1, "Rome", attackableCities, actions)
//  val (updatedGameState, _) = game.gameTurn(aiActionResult).run(game)
//  println("\n>> Final sabotaged cities: " + updatedGameState.ai.sabotagedCities)
//package controller
//
//import controller.InputHandling.InputHandlingError
//import model.map.WorldMapModule.WorldMap
//import model.map.WorldState.{WorldState, createWorldState}
//import model.strategy.*
//import model.strategy.PlayerAI
//import model.strategy
//import model.util.States.State.State
//import view.ViewModule.{CLIView, GameView}
//
//object GameController:
//  /**
//   * Crea un nuovo controller di gioco con i componenti predefiniti
//   *
//   * @return Un nuovo GameController
//   */
//  def apply(): GameState =
//    val (ai, human, worldMap) = model.GameFactory.createGame()
//    val view = CLIView
//    val worldState = createWorldState(worldMap,ai,human)
//    GameState(worldState,view)
//
//case class GameState(worldState: WorldState,
//                      view: GameView):
//
//
//
//  import model.util.States.State.State
//
//  private def doPlayerAction(action: AiAction): State[GameState, Unit] = ???
////    State { gs =>
////      val result = worldState.playerAI.executeAction(action, worldMap)
////      val updatedAi = result.getPlayer
////      val maybeCity = result.getCity
////      (worldState.updatePlayer(updatedAi), ())
////   }
//
//  private def doHumanAction(action: HumanAction): State[GameState, Unit] = ???
//   // State { gs => (gs.copy(human = gs.getCurrentHuman.executeAction(action)),())}
//
//  private def renderTurn(): State[GameState,Unit] =
//    val abilities: Set[String] = Set.empty
//    val options = List("SabotageAction", "InfectAction", "EvolveAction","Exit")
//    State { state => view.renderGameTurn(worldState)
//      (state, ())
//    }
//
//  def gameTurn(aiActionResult: Either[InputHandlingError, AiAction]): State[GameState, Unit] =
//    aiActionResult match {
//      case Right(aiAction) =>
//        for
//          _ <- renderTurn()
//          _ <- doPlayerAction(aiAction)
//        // _ <- doHumanAction(humanAction)
//        yield()
//      case Left(error) => ???
//    }
//
//
//  /*
//  def startGame() : Unit =
//    //TODO: used to create a dedicated test
//    val actions = List(SabotageAction(), InfectAction(), EvolveAction())
//    val userChoice = view.getInputForAction(List("Sabotage", "Infect", "Evolve")) // ottieni l'input
//    val aiActionResult = InputHandler.getActionFromChoice(userChoice, actions)
//
//    aiActionResult match {
//      case Right(aiAction) =>
//        doPlayerAction(aiAction)
//      case Left(error) =>
//        view.renderActionMenu(List("Sabotage", "Infect", "Evolve"))
//    }
//  */
//
////@main def tryController(): Unit =
////  val game = GameController.apply()
////  val actions = List(SabotageAction(), InfectAction(), EvolveAction()) //ai.getPossibleActions
////  val attackableCities = Set("Rome", "Paris", "Berlin")
////  val aiActionResult = InputHandler.getActionFromChoice(1, "Rome", attackableCities, actions)
////  val (updatedGameState, _) = game.gameTurn(aiActionResult).run(game)
////  println("\n>> Final sabotaged cities: " + updatedGameState.ai.sabotagedCities)
