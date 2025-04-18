package model

import model.strategy.{Ability, PlayerAI, TurnAction}
import model.strategy.turnAction.{EvolveAction, InfectAction, SabotageAction}
import org.junit.*
import org.junit.Assert.assertEquals

class PlayerAITest :
  var player : PlayerAI = _
  var action : TurnAction = _
  var cities: List[String] = List("Milan", "Rome")

  @Before
  def init(): Unit =
    player = PlayerAI()
  @Test
  def applyEvolveAbilityTest() : Unit =
    action = EvolveAction()
    val updatedPlayer = player.executeAction(action)
    assert(updatedPlayer.unlockedAbilities.nonEmpty)
    if (updatedPlayer.unlockedAbilities.contains(Ability.ImprovedInfection)) {
      assert(updatedPlayer.infectionChance == player.infectionChance + 10)
    }
    if (updatedPlayer.unlockedAbilities.contains(Ability.StealthSabotage)) {
      assert(updatedPlayer.sabotagePower == player.sabotagePower + 5)
    }

  @Test
  def applyMultipleEvolveActionsTest(): Unit =
    action = EvolveAction()
    val playerAfterFirstEvolve = player.executeAction(action)
    action = EvolveAction()
    val playerAfterSecondEvolve = playerAfterFirstEvolve.executeAction(action)
    assert(playerAfterSecondEvolve.executedActions.size == 2)
    assert(playerAfterSecondEvolve.infectionChance == player.infectionChance + 10)
    assert(playerAfterSecondEvolve.sabotagePower == player.sabotagePower + 5)


  @Test
  def applyInfectActionTest(): Unit =
    action = InfectAction(Some(cities))
    val updatedPlayer = player.executeAction(action)

    assert(updatedPlayer.conqueredCities.contains("Milan"))
    assert(updatedPlayer.conqueredCities.contains("Rome"))
    assert(updatedPlayer.conqueredCities.size == 2)

  @Test
  def applySabotageActionTest(): Unit =
    action = SabotageAction(Some(cities))
    val updatedPlayer = player.executeAction(action)

    assert(updatedPlayer.sabotagedCities.contains("Milan"))
    assert(updatedPlayer.sabotagedCities.contains("Rome"))
    assert(updatedPlayer.sabotagedCities.size == 2)