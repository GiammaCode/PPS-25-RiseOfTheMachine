package model

import model.strategy.{CityDefense, DevelopKillSwitch, GlobalDefense, TurnAction}
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals

class HumanActiontest:
  var action: TurnAction = _
  val singleTarget: List[String] = List("Milan")
  val targets: List[String] = List("Milan", "Rome")

  @Test
  def singleCityDefenseTest(): Unit =
    action = CityDefense(singleTarget)
    val result = action.execute
    assertEquals("CityDefenseAction on List(Milan)", result)

  @Test
  def allCityDefenseTest(): Unit =
    action = GlobalDefense(targets)
    val result = action.execute
    assertEquals("GlobalDefenseAction on List(Milan, Rome)", result)

  @Test
  def developKillSwitchTest(): Unit =
    action = DevelopKillSwitch
    val result = action.execute
    assertEquals("DevelopKillSwitchAction is done", result)

