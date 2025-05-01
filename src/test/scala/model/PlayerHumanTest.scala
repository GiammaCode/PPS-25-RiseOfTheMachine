package model

import model.strategy.{CityDefense, DevelopKillSwitch, GlobalDefense, HumanAction, PlayerHuman}
import org.junit.*
import org.junit.Assert.{assertEquals, assertTrue}

class PlayerHumanTest :
  var human : PlayerHuman = _
  var action : HumanAction = _
  var cities: List[String] = List("Milan", "Rome")
  var city: List[String] = List("Milan")

  @Before
  def init(): Unit =
    human = PlayerHuman.default

  @Test
  def applyGlobalDefenseTest() : Unit=
    action = GlobalDefense(cities)
    val updateHuman = human.executeAction(action).getPlayer
    assertTrue(updateHuman.executedActions.nonEmpty)

  @Test
  def applyCityDefenseTest() : Unit =
    action = CityDefense(city)
    val updateHuman = human.executeAction(action).getPlayer
    assertTrue(updateHuman.executedActions.nonEmpty)


  @Test
  def applyDevelopKillSwitchTest() : Unit =
    action = DevelopKillSwitch
    val updateHuman = human.executeAction(action).getPlayer
    assertTrue(updateHuman.executedActions.nonEmpty)

  @Test
  def applyMultipleAction() : Unit =
    val firstAction : HumanAction = DevelopKillSwitch
    val firstActionHuman = human.executeAction(firstAction).getPlayer
    assertTrue(firstActionHuman.executedActions.nonEmpty)

    val secondAction : HumanAction = CityDefense(city)
    val secondActionHuman = firstActionHuman.executeAction(secondAction).getPlayer
    assertEquals(2, secondActionHuman.executedActions.size)

  @Test
  def HumanToStringTest() : Unit =
    val defendedActionHuman = human.executeAction(CityDefense(List("Milan")))
    val output = defendedActionHuman.toString
    print(output)

    assert(output.contains("Conquered Cities"))
    assert(output.contains("Defended Cities"))
    assert(output.contains("Executed Actions"))









