package model

import model.strategy.turnAction.{InfectAction, SabotageAction}
import model.strategy.{TurnAction, TurnActionType}
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals


class  ActionTest:
  var action : TurnAction = _
  var cityList: Option[List[String]] = Some(List("Milan", "Rome"))

  @Test
  def executeInfectActionTest(): Unit = {
    action = InfectAction(cityList)
    val result = action.execute
    assertEquals("InfectAction on Some(List(Milan, Rome))", result)
  }

  @Test
  def executeSabotageActionTest(): Unit = {
    action = SabotageAction(cityList)
    val result = action.execute
    assertEquals("SabotageAction on Some(List(Milan, Rome))", result)
  }



