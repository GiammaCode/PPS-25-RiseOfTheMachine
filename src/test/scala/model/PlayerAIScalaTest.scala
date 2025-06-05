package model

import model.map.WorldMapModule.createWorldMap
import model.map.WorldState.*
import model.strategy.*
import model.util.GameSettings.*
import model.util.GameSettings.DifficultyConstants.*
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlayerAIScalaTest extends AnyFunSuite with Matchers with BeforeAndAfter:

  var player: PlayerAI = _
  var worldState: WorldState = _
  val testCities: Seq[ActionTarget] = List("TestCity1", "TestCity2", "TestCity3")
  val infectionPower = 25
  val sabotagePower = 75

  given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Easy)

  before {
    player = PlayerAI.fromSettings
    worldState = createWorldState(createWorldMap(easyInfectionPower), PlayerAI.fromStats, PlayerHuman.fromStats, 0)
  }


  test("PlayerAI creation with different difficulty levels") {
    given easySettings: GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Easy)
    given normalSettings: GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)
    given hardSettings: GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Hard)

    val easyPlayer = PlayerAI.fromSettings(using easySettings)
    val normalPlayer = PlayerAI.fromSettings(using normalSettings)
    val hardPlayer = PlayerAI.fromSettings(using hardSettings)

    easyPlayer.infectionPower shouldBe easyInfectionPower
    easyPlayer.sabotagePower shouldBe easySabotagePower

    normalPlayer.infectionPower shouldBe normalInfectionPower
    normalPlayer.sabotagePower shouldBe normalSabotagePower

    hardPlayer.infectionPower shouldBe hardInfectionPower
    hardPlayer.sabotagePower shouldBe hardSabotagePower
  }

  test("PlayerAI fromStats constructor") {
    given testStats: AIStats = {

      AIStats(infectionPower = infectionPower, sabotagePower = sabotagePower)
    }
    val playerFromStats = PlayerAI.fromStats(using testStats)

    playerFromStats.infectionPower shouldBe 25
    playerFromStats.sabotagePower shouldBe 75
    playerFromStats.unlockedAbilities shouldBe empty
    playerFromStats.executedActions shouldBe empty
    playerFromStats.conqueredCities shouldBe empty
    playerFromStats.sabotagedCities shouldBe empty
  }

  test("Initial PlayerAI state is correct") {
    player.unlockedAbilities shouldBe empty
    player.executedActions shouldBe empty
    player.conqueredCities shouldBe empty
    player.sabotagedCities shouldBe empty
    player.infectionPower shouldBe easyInfectionPower
    player.sabotagePower shouldBe easySabotagePower
  }

  test("getPossibleAction returns all actions when no abilities unlocked") {
    val possibleActions = player.getPossibleAction
    possibleActions should contain(Evolve)
    possibleActions.exists(_.isInstanceOf[Infect]) shouldBe true
    possibleActions.exists(_.isInstanceOf[Sabotage]) shouldBe true
  }

  test("getPossibleAction excludes Evolve when all abilities are unlocked") {
    val fullyEvolvedPlayer = PlayerAI.fromSettings.executeAction(Evolve, worldState.worldMap).getPlayer
      .executeAction(Evolve, worldState.worldMap).getPlayer

    val possibleActions = fullyEvolvedPlayer.getPossibleAction
    possibleActions should not contain Evolve
  }

  test("Evolve action when no abilities remain to unlock") {
    val playerWithOneAbility = player.executeAction(Evolve, worldState.worldMap).getPlayer
    val fullyEvolvedPlayer = playerWithOneAbility.executeAction(Evolve, worldState.worldMap).getPlayer

    val result = fullyEvolvedPlayer.executeAction(Evolve, worldState.worldMap)
    val finalPlayer = result.getPlayer

    finalPlayer.executedActions should have size 3
    finalPlayer.executedActions.head shouldBe Evolve
  }

  test("Infect action with empty city list") {
    val result = player.executeAction(Infect(List.empty), worldState.worldMap)
    val updatedPlayer = result.getPlayer

    updatedPlayer.conqueredCities shouldBe empty
    updatedPlayer.executedActions should have size 1
    updatedPlayer.executedActions.head shouldBe Infect(List.empty)
  }

  test("Sabotage action with empty city list") {
    val result = player.executeAction(Sabotage(List.empty), worldState.worldMap)
    val updatedPlayer = result.getPlayer

    updatedPlayer.sabotagedCities shouldBe empty
    updatedPlayer.executedActions should have size 1
    updatedPlayer.executedActions.head shouldBe Sabotage(List.empty)
  }

  test("Infect action with non-existent cities") {
    val nonExistentCities = List("NonExistent1", "NonExistent2")
    val result = player.executeAction(Infect(nonExistentCities), worldState.worldMap)
    val updatedPlayer = result.getPlayer

    updatedPlayer.conqueredCities should contain allElementsOf List("NonExistent1", "NonExistent2")
    updatedPlayer.executedActions should have size 1
    updatedPlayer.executedActions.head shouldBe Infect(nonExistentCities)
  }

  test("Sabotage action with non-existent cities") {
    val nonExistentCities = List("NonExistent1", "NonExistent2")
    val result = player.executeAction(Sabotage(nonExistentCities), worldState.worldMap)
    val updatedPlayer = result.getPlayer

    updatedPlayer.sabotagedCities should contain allElementsOf List("NonExistent1", "NonExistent2")
    updatedPlayer.executedActions should have size 1
    updatedPlayer.executedActions.head shouldBe Sabotage(nonExistentCities)
  }

  test("Multiple consecutive actions maintain correct order in executedActions") {
    val cities1 = List("City1", "City2")
    val cities2 = List("City3", "City4")

    val step1 = player.executeAction(Infect(cities1), worldState.worldMap).getPlayer
    val step2 = step1.executeAction(Evolve, worldState.worldMap).getPlayer
    val step3 = step2.executeAction(Sabotage(cities2), worldState.worldMap).getPlayer

    step3.executedActions should have size 3
    step3.executedActions(0) shouldBe Sabotage(cities2)
    step3.executedActions(1) shouldBe Evolve
    step3.executedActions(2) shouldBe Infect(cities1)
  }

  test("Infect action accumulates conquered cities across multiple calls") {
    val cities1 = List("City1", "City2")
    val cities2 = List("City3", "City4")

    val step1 = player.executeAction(Infect(cities1), worldState.worldMap).getPlayer
    val step2 = step1.executeAction(Infect(cities2), worldState.worldMap).getPlayer

    step2.conqueredCities should contain allElementsOf List("City1", "City2", "City3", "City4")
    step2.conqueredCities should have size 4
  }

  test("Sabotage action accumulates sabotaged cities across multiple calls") {
    val cities1 = List("City1", "City2")
    val cities2 = List("City3", "City4")

    val step1 = player.executeAction(Sabotage(cities1), worldState.worldMap).getPlayer
    val step2 = step1.executeAction(Sabotage(cities2), worldState.worldMap).getPlayer

    step2.sabotagedCities should contain allElementsOf List("City1", "City2", "City3", "City4")
    step2.sabotagedCities should have size 4
  }

  test("Duplicate cities in infect action are handled correctly") {
    val citiesWithDuplicates = List("City1", "City2", "City1", "City3", "City2")
    val result = player.executeAction(Infect(citiesWithDuplicates), worldState.worldMap)
    val updatedPlayer = result.getPlayer

    updatedPlayer.conqueredCities should contain allElementsOf List("City1", "City2", "City3")
    updatedPlayer.conqueredCities should have size 3
  }

  test("Duplicate cities in sabotage action are handled correctly") {
    val citiesWithDuplicates = List("City1", "City2", "City1", "City3", "City2")
    val result = player.executeAction(Sabotage(citiesWithDuplicates), worldState.worldMap)
    val updatedPlayer = result.getPlayer

    updatedPlayer.sabotagedCities should contain allElementsOf List("City1", "City2", "City3")
    updatedPlayer.sabotagedCities should have size 3
  }

  test("toString method with populated data") {
    val cities = List("TestCity1", "TestCity2")
    val evolvedPlayer = player.executeAction(Evolve, worldState.worldMap).getPlayer
    val infectedPlayer = evolvedPlayer.executeAction(Infect(cities), worldState.worldMap).getPlayer
    val sabotagedPlayer = infectedPlayer.executeAction(Sabotage(cities), worldState.worldMap).getPlayer
    val playerString = sabotagedPlayer.toString

    playerString should include("--- PlayerAI Status ---")
    playerString should not include "None"
    playerString should include("TestCity1")
    playerString should include("TestCity2")
    sabotagedPlayer.unlockedAbilities.foreach { ability =>
      playerString should include(ability.toString)
    }
  }

