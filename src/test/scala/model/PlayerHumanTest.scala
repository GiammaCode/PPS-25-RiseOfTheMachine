package model

import model.strategy.HumanActionTypes.DevelopKillSwitch
import model.strategy.PlayerHuman.PlayerHuman
import model.strategy.humanActions.{CityDefenseAction, DevelopKillSwitchAction, GlobalDefenseAction}
import model.strategy.{HumanAction, PlayerHuman}
import model.strategy.playerActions.*
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
    action = GlobalDefenseAction(cities)
    val updateHuman = human.executeAction(action)
    assertTrue(updateHuman.executedActions.nonEmpty)

  @Test
  def applyCityDefenseTest() : Unit =
    action = CityDefenseAction(city)
    val updateHuman = human.executeAction(action)
    assertTrue(updateHuman.executedActions.nonEmpty)


  @Test
  def applyDevelopKillSwitchTest() : Unit =
    action = DevelopKillSwitchAction(city)
    val updateHuman = human.executeAction(action)
    assertTrue(updateHuman.executedActions.nonEmpty)

  @Test
  def applyMultipleAction() : Unit =
    val firstAction : HumanAction = DevelopKillSwitchAction(city)
    val firstActionHuman = human.executeAction(firstAction)
    assertTrue(firstActionHuman.executedActions.nonEmpty)

    val secondAction : HumanAction = CityDefenseAction(city)
    val secondActionHuman = firstActionHuman.executeAction(secondAction)
    assertEquals(2, secondActionHuman.executedActions.size)

  @Test
  def HumanToStringTest() : Unit =
    val defendedActionHuman = human.executeAction(CityDefenseAction(List("Milan")))
    val output = defendedActionHuman.toString
    print(output)

    assert(output.contains("Conquered Cities"))
    assert(output.contains("Defended Cities"))
    assert(output.contains("Executed Actions"))









