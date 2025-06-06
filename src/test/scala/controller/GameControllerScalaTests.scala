package controller

import controller.GameController.*
import model.map.CityModule.Owner
import model.map.WorldMapModule
import model.map.WorldMapModule.CreateModuleType
import model.map.WorldState.WorldState
import model.strategy.{AiAction, HumanAction}
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings, gameMode, gameSettings}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.io.ByteArrayInputStream

class GameControllerScalaTests extends AnyFunSuite with Matchers {
  given GameSettings = forSettings(GameMode.Singleplayer,Difficulty.Normal)
  given CreateModuleType = WorldMapModule.DeterministicMapModule

  val gameState: GameState = buildGameState
  val initialState: WorldState = gameState.worldState
  val targetCity = "A"
  private val sicureProbability = 100
  val increaseDefense = 20
  val defenceDecreasedBySabotage = 40





  test("buildGameState should initialize a valid GameState"):
    gameState.worldState.playerAI should not be null
    gameState.worldState.playerHuman should not be null
    gameState.worldState.worldMap.numberOfCity() should be > 0


  test("test humanAction develop kill switch"):
    val testAction: Option[HumanAction] = Some(HumanAction.developKillSwitch())
    val ( updatedState,_) = doHumanAction(testAction).run(gameState)
    assert(updatedState.worldState.playerHuman != gameState.worldState.playerHuman)


  test("test humanAction cityDefense"):
    val testAction: Option[HumanAction] = Some(HumanAction.cityDefense(List(targetCity)))
    val (updatedState, _) = doHumanAction(testAction).run(gameState)

    updatedState.worldState.worldMap.getCityByName(targetCity).get.getDefense shouldBe
     gameState.worldState.worldMap.getCityByName(targetCity).get.getDefense + increaseDefense


  test("test humanAction globalDefence"):
    val testAction: Option[HumanAction] = Some(HumanAction.globalDefense(List(targetCity)))
    val (updatedState, _) = doHumanAction(testAction).run(gameState)

    updatedState.worldState.worldMap.getCityByName(targetCity).get.getDefense shouldBe
      gameState.worldState.worldMap.getCityByName(targetCity).get.getDefense + 20


  test("test player evolve"):
    val (updatedState, _) = doPlayerAction(AiAction.evolve(),sicureProbability).run(gameState)
    updatedState.worldState.playerAI should not be gameState.worldState.playerAI


  test("test player infect"):
    val (updatedState, _) = doPlayerAction(AiAction.infect(List(targetCity)), sicureProbability).run(gameState)
    updatedState.worldState.worldMap.getCityByName(targetCity).get.getOwner shouldBe Owner.AI


  test("test player sabotage"):
    val (updatedState, _) = doPlayerAction(AiAction.sabotage(List(targetCity)), sicureProbability).run(gameState)
    updatedState.worldState.worldMap.getCityByName(targetCity).get.getDefense shouldBe
      gameState.worldState.worldMap.getCityByName(targetCity).get.getDefense - defenceDecreasedBySabotage






}
