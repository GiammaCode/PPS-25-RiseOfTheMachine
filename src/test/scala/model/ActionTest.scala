package model

import model.strategy.turnAction.InfectAction
import model.strategy.{TurnAction, TurnActionType}
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals


class  ActionTest:
  var action : TurnAction = _

  @Before
  def initialize(): Unit = {
    action = InfectAction(Some(List("Milan", "Rome")))
  }

  @Test
  def executeActionTest(): Unit = {
    val result = action.execute
    assertEquals("InfectAction on Some(List(Milan, Rome))", result)
  }

