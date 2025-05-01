package model

import model.strategy.{AiAbility, AiAction, Evolve, Infect, PlayerAI, Sabotage, TurnAction}
import org.junit.*
import org.junit.Assert.assertEquals

class PlayerAITest :
  var player : PlayerAI = _
  var action : AiAction = _
  var cities: List[String] = List("Milan", "Rome")

  @Before
  def init(): Unit =
    player = PlayerAI.default
  @Test
  def applyEvolveAbilityTest() : Unit =
    action = Evolve
    val updatedPlayer = player.executeAction(action).getPlayer
    assert(updatedPlayer.unlockedAbilities.nonEmpty)
    if (updatedPlayer.unlockedAbilities.contains(AiAbility.ImprovedInfection)) {
      assert(updatedPlayer.infectionChance == player.infectionChance + 10)
    }
    if (updatedPlayer.unlockedAbilities.contains(AiAbility.StealthSabotage)) {
      assert(updatedPlayer.sabotagePower == player.sabotagePower + 5)
    }

  @Test
  def applyMultipleEvolveActionsTest(): Unit =
    action = Evolve
    val playerAfterFirstEvolve = player.executeAction(action).getPlayer
    action = Evolve
    val playerAfterSecondEvolve = playerAfterFirstEvolve.executeAction(action).getPlayer
    assert(playerAfterSecondEvolve.executedActions.size == 2)
    assert(playerAfterSecondEvolve.infectionChance == player.infectionChance + 10)
    assert(playerAfterSecondEvolve.sabotagePower == player.sabotagePower + 5)


  @Test
  def applyInfectActionTest(): Unit =
    action = Infect(cities)
    val updatedPlayer = player.executeAction(action).getPlayer

    assert(updatedPlayer.conqueredCities.contains("Milan"))
    assert(updatedPlayer.conqueredCities.contains("Rome"))
    assert(updatedPlayer.conqueredCities.size == 2)

  @Test
  def applySabotageActionTest(): Unit =
    action = Sabotage(cities)
    val updatedPlayer = player.executeAction(action).getPlayer

    assert(updatedPlayer.sabotagedCities.contains("Milan"))
    assert(updatedPlayer.sabotagedCities.contains("Rome"))
    assert(updatedPlayer.sabotagedCities.size == 2)
