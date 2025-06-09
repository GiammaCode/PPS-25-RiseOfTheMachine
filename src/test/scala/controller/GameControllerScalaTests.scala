package controller

import controller.GameController.*
import model.map.CityModule.Owner
import model.map.WorldMapModule
import model.map.WorldMapModule.CreateModuleType
import model.map.WorldState.WorldState
import model.strategy.{AiAction, HumanAction}
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameControllerScalaTests extends AnyFunSuite with Matchers {
  given GameSettings = forSettings(GameMode.Singleplayer,Difficulty.Normal)
  given CreateModuleType = WorldMapModule.DeterministicMapModule

  val gameState: GameState = buildGameState
  val initialState: WorldState = gameState.worldState
  val targetCityA = "A"
  private val sicureProbability = 100
  val increaseDefense = 20
  val defenceDecreasedBySabotage = 40
  val targetCityB = "B"





  test("buildGameState should initialize a valid GameState"):
    gameState.worldState.playerAI should not be null
    gameState.worldState.playerHuman should not be null
    gameState.worldState.worldMap.numberOfCity() should be > 0


  test("test humanAction develop kill switch"):
    val testAction: Option[HumanAction] = Some(HumanAction.developKillSwitch())
    val ( updatedState,_) = doHumanAction(testAction).run(gameState)
    assert(updatedState.worldState.playerHuman != gameState.worldState.playerHuman)


  test("test humanAction cityDefense"):
    val testAction: Option[HumanAction] = Some(HumanAction.cityDefense(List(targetCityA)))
    val (updatedState, _) = doHumanAction(testAction).run(gameState)

    updatedState.worldState.worldMap.getCityByName(targetCityA).get.getDefense shouldBe
     gameState.worldState.worldMap.getCityByName(targetCityA).get.getDefense + increaseDefense


  test("test humanAction globalDefence"):
    val ListOfCity = List("A", "B", "C")
    val testAction: Option[HumanAction] = Some(HumanAction.globalDefense(ListOfCity))
    val (updatedState, _) = doHumanAction(testAction).run(gameState)

    val globalDefenseBoost = 2
    updatedState.worldState.worldMap.getCityByName(targetCityA).get.getDefense shouldBe
      gameState.worldState.worldMap.getCityByName(targetCityA).get.getDefense + globalDefenseBoost

    val wrongDefenseBoost = 5
    updatedState.worldState.worldMap.getCityByName(targetCityB).get.getDefense should not be
      gameState.worldState.worldMap.getCityByName(targetCityB).get.getDefense + wrongDefenseBoost




  test("test player evolve"):
    val (updatedState, _) = doPlayerAction(AiAction.evolve(),sicureProbability).run(gameState)
    updatedState.worldState.playerAI should not be gameState.worldState.playerAI


  test("test player infect"):
    val (updatedState, _) = doPlayerAction(AiAction.infect(List(targetCityA)), sicureProbability).run(gameState)
    updatedState.worldState.worldMap.getCityByName(targetCityA).get.getOwner shouldBe Owner.AI


  test("test player sabotage"):
    val (updatedState, _) = doPlayerAction(AiAction.sabotage(List(targetCityA)), sicureProbability).run(gameState)
    updatedState.worldState.worldMap.getCityByName(targetCityA).get.getDefense shouldBe
      gameState.worldState.worldMap.getCityByName(targetCityA).get.getDefense - defenceDecreasedBySabotage


  test("test render turn"):
    val simulatedInput = new java.io.ByteArrayInputStream("2\n".getBytes)
    val outputBufferMenu = new java.io.ByteArrayOutputStream()
    val outputBuffer = new java.io.ByteArrayOutputStream()

    val result: Unit = Console.withIn(simulatedInput) {
      Console.withOut(outputBuffer) {
        val(updateState,_) = gameTurn.run(gameState)
        1 shouldBe updateState.worldState.playerAI.executedActions.size
        updateState.worldState.playerAI.executedActions.head shouldBe a [AiAction]
        1 shouldBe updateState.worldState.playerHuman.executedActions.size
        updateState.worldState.playerHuman.executedActions.head shouldBe a[HumanAction]
      }
    }









}
