package model

import model.strategy.turnAction.{EvolveAction, InfectAction, SabotageAction}
import model.strategy.{TurnAction, TurnActionType}
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals


class  ActionTest:
  var action : TurnAction = _
  var cityList: Option[List[String]] = Some(List("Milan", "Rome"))

  var value: Option[List[String]] = Some(List("Milan", "Rome"))

  @Test
  def executeInfectActionTest(): Unit = {
    action = InfectAction(cityList)
    val result = action.execute
    assertEquals("InfectAction on " + value, result)
  }

  @Test
  def executeSabotageActionTest(): Unit = {
    action = SabotageAction(cityList)
    val result = action.execute
    assertEquals("SabotageAction on " + value, result)
  }

  @Test
  def executeEvolveActionTest() : Unit = {
    action = EvolveAction()
    val result = action.execute
    assertEquals("Evolving", result)
  }



