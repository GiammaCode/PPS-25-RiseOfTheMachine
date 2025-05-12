package model

import controller.GameController.buildGameState
import model.map.WorldMapModule.createWorldMap
import model.strategy.{AiAbility, AiAction, Evolve, Infect, PlayerAI, PlayerHuman, Sabotage, TurnAction}
import org.junit.*
import org.junit.Assert.assertEquals
import model.map.WorldState.*
import model.util.GameDifficulty
import model.util.GameDifficulty.Difficulty
import model.util.GameDifficulty.Difficulty.{Easy, Hard, Normal}

class PlayerAITest :
  var player : PlayerAI = _
  var cities: List[String] = List("A", "B")
  var worldState : WorldState = _
  given Difficulty = Difficulty.Easy // TODO: get from CLI


  @Before
  def init(): Unit =
    player = PlayerAI.fromDifficulty(Normal)
    worldState = createWorldState( createWorldMap(10), PlayerAI.fromStats, PlayerHuman.fromStats)

  @Test
  def applyEvolveAbilityTest() : Unit =
    val updatedPlayer = player.executeAction(Evolve, worldState.worldMap).getPlayer
    assert(updatedPlayer.unlockedAbilities.nonEmpty)
    if (updatedPlayer.unlockedAbilities.contains(AiAbility.ImprovedInfection)) {
      assert(updatedPlayer.infectionChance == player.infectionChance + 10)
    }
    if (updatedPlayer.unlockedAbilities.contains(AiAbility.StealthSabotage)) {
      assert(updatedPlayer.sabotagePower == player.sabotagePower + 5)
    }

  @Test
  def applyMultipleEvolveActionsTest(): Unit =
    val playerAfterFirstEvolve = player.executeAction(Evolve, worldState.worldMap).getPlayer
    val playerAfterSecondEvolve = playerAfterFirstEvolve.executeAction(Evolve, worldState.worldMap).getPlayer
    assert(playerAfterSecondEvolve.executedActions.size == 2)
    assert(playerAfterSecondEvolve.infectionChance == player.infectionChance + 10)
    assert(playerAfterSecondEvolve.sabotagePower == player.sabotagePower + 5)


  @Test
  def applyInfectActionTest(): Unit =
    val updatedPlayer = player.executeAction(Infect(cities), worldState.worldMap).getPlayer

    assert(updatedPlayer.conqueredCities.contains("A"))
    assert(updatedPlayer.conqueredCities.contains("B"))
    assert(updatedPlayer.conqueredCities.size == 2)

  @Test
  def applySabotageActionTest(): Unit =
    val updatedPlayer = player.executeAction(Sabotage(cities), worldState.worldMap).getPlayer
    assert(updatedPlayer.sabotagedCities.contains("A"))
    assert(updatedPlayer.sabotagedCities.contains("B"))
    assert(updatedPlayer.sabotagedCities.size == 2)

  @Test
  def easyDifficultyStatsTest(): Unit =
    val easyPlayer = PlayerAI.fromDifficulty(Easy)
    val expected = GameDifficulty.aiStatsFor(Easy)
    assertEquals(expected.infectionChance, easyPlayer.infectionChance)
    assertEquals(expected.sabotagePower, easyPlayer.sabotagePower)

  @Test
  def hardDifficultyStatsTest(): Unit =
    val hardPlayer = PlayerAI.fromDifficulty(Hard)
    val expected = GameDifficulty.aiStatsFor(Hard)
    assertEquals(expected.infectionChance, hardPlayer.infectionChance)
    assertEquals(expected.sabotagePower, hardPlayer.sabotagePower)

