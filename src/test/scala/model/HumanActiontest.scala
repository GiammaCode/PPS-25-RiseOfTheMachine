package model

import model.strategy.HumanActions.*
import model.strategy.{TurnAction, TurnActionType}
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals

class HumanActiontest:
  var action: TurnAction = _
  val singleTarget: List[String] = List("Milan")
  val targets: List[String] = List("Milan", "Rome")

  @Test
  def singleCityDefenseTest(): Unit =
    action = CityDefenseAction(singleTarget)
    val result = action.execute
    assertEquals("CityDefenseAction on List(Milan)", result)

  @Test
  def allCityDefenseTest(): Unit =
    action = GlobalDefenseAction(targets)
    val result = action.execute
    assertEquals("GlobalDefenseAction on List(Milan, Rome)", result)

  @Test
  def developKillSwitchTest(): Unit =
    action = DevelopKillSwitchAction()
    val result = action.execute
    assertEquals("DevelopKillSwitchAction is done", result)

