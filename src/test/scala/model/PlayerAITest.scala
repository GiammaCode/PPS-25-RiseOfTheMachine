package model

import controller.GameController.buildGameState
import model.map.WorldMapModule.createWorldMap
import model.strategy.{AiAbility, AiAction, Evolve, Infect, PlayerAI, PlayerHuman, Sabotage, TurnAction}
import org.junit.*
import org.junit.Assert.assertEquals
import model.map.WorldState.*
import model.strategy.AiAbilityValues.{ImprovedInfectionBonusPerc, StealthSabotageBonusPerc}
import model.util.GameSettings.*

class PlayerAITest :
  var player : PlayerAI = _
  var cities: List[String] = List("A", "B")
  var worldState : WorldState = _
  given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Easy)


  private val EasyPlayerInfcPwr = 10

  @Before
  def init(): Unit =
    player = PlayerAI.fromSettings
    worldState = createWorldState( createWorldMap(EasyPlayerInfcPwr), PlayerAI.fromStats, PlayerHuman.fromStats, 0)

  private val EasyPlayerSabPwr = 50
  private val BasicPercBonus = 50


  @Test
  def applyEvolveAbilityTest() : Unit =
    val updatedPlayer = player.executeAction(Evolve, worldState.worldMap).getPlayer
    assert(updatedPlayer.unlockedAbilities.nonEmpty)
    if (updatedPlayer.unlockedAbilities.contains(AiAbility.ImprovedInfection)) {
      assertEquals(updatedPlayer.infectionPower, math.round(EasyPlayerInfcPwr * (1 + ImprovedInfectionBonusPerc / 100.0)).toInt)
    }
    if (updatedPlayer.unlockedAbilities.contains(AiAbility.StealthSabotage)) {
      assertEquals(updatedPlayer.sabotagePower ,math.round(EasyPlayerSabPwr * (1 + StealthSabotageBonusPerc / 100.0)).toInt)
    }

  @Test
  def applyMultipleEvolveActionsTest(): Unit =
    val playerAfterFirstEvolve = player.executeAction(Evolve, worldState.worldMap).getPlayer
    val playerAfterSecondEvolve = playerAfterFirstEvolve.executeAction(Evolve, worldState.worldMap).getPlayer
    assertEquals(playerAfterSecondEvolve.executedActions.size, 2)
    assertEquals(playerAfterSecondEvolve.infectionPower ,math.round(EasyPlayerInfcPwr * (1 + ImprovedInfectionBonusPerc / 100.0)).toInt)
    assertEquals(playerAfterSecondEvolve.sabotagePower , math.round(EasyPlayerSabPwr * (1 + StealthSabotageBonusPerc / 100.0)).toInt)


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

