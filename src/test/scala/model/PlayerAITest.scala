package model

import model.strategy.playerActions.*
import model.strategy.{AiAbility, PlayerAI, AiAction, TurnAction}
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
    action = EvolveAction()
    val updatedPlayer = player.executeAction(action)
    assert(updatedPlayer.unlockedAbilities.nonEmpty)
    if (updatedPlayer.unlockedAbilities.contains(AiAbility.ImprovedInfection)) {
      assert(updatedPlayer.infectionChance == player.infectionChance + 10)
    }
    if (updatedPlayer.unlockedAbilities.contains(AiAbility.StealthSabotage)) {
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
    action = InfectAction(cities)
    val updatedPlayer = player.executeAction(action)

    assert(updatedPlayer.conqueredCities.contains("Milan"))
    assert(updatedPlayer.conqueredCities.contains("Rome"))
    assert(updatedPlayer.conqueredCities.size == 2)

  @Test
  def applySabotageActionTest(): Unit =
    action = SabotageAction(cities)
    val updatedPlayer = player.executeAction(action)

    assert(updatedPlayer.sabotagedCities.contains("Milan"))
    assert(updatedPlayer.sabotagedCities.contains("Rome"))
    assert(updatedPlayer.sabotagedCities.size == 2)

  @Test
  def playerToStringTest(): Unit =
    val infectedPlayer = player.executeAction(InfectAction(List("Milan")))
    val sabotagedPlayer = infectedPlayer.executeAction(SabotageAction(List("Rome")))
    val evolvedPlayer = sabotagedPlayer.executeAction(EvolveAction())

    val output = evolvedPlayer.toString
    print(output)

    assert(output.contains("Unlocked Abilities"))
    assert(output.contains("Infection Chance"))
    assert(output.contains("Sabotage Power"))
    assert(output.contains("Conquered Cities"))
    assert(output.contains("Sabotaged Cities"))
    assert(output.contains("Executed Actions"))

    assert(output.contains("Milan"))
    assert(output.contains("Rome"))
    assert(output.contains("InfectAction"))
    assert(output.contains("SabotageAction"))
    assert(output.contains("Evolving")) // da EvolveAction
