package model

import model.strategy.turnAction.{CityDefenseAction, DevelopKillSwitchAction, GlobalDefenseAction}
import model.strategy.{TurnAction, TurnActionType}
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals

class HumanActiontest:
  var action: TurnAction = _
  val singleTarget: Option[List[String]] = Some(List("Milan"))
  val targets: Option[List[String]] = Some(List("Milan", "Rome"))

  @Test
  def singleCityDefenseTest(): Unit =
    action = CityDefenseAction(singleTarget)
    val result = action.execute
    assertEquals("CityDefenseAction on Some(List(Milan))", result)

  @Test
  def allCityDefenseTest(): Unit =
    action = GlobalDefenseAction(targets)
    val result = action.execute
    assertEquals("GlobalDefenseAction on Some(List(Milan, Rome))", result)

  @Test
  def developKillSwitchTest(): Unit =
    action = DevelopKillSwitchAction()
    val result = action.execute
    assertEquals("DevelopKillSwitchAction is done", result)

