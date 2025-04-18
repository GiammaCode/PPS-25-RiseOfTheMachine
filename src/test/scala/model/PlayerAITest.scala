package model

import model.strategy.{Ability, PlayerAI, TurnAction}
import model.strategy.turnAction.EvolveAction
import org.junit.*
import org.junit.Assert.assertEquals

class PlayerAITest :
  var player : PlayerAI = _
  var action : TurnAction = _

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
