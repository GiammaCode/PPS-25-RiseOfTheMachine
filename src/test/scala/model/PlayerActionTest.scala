package model


import model.strategy.TurnAction
import model.strategy.playerActions.*
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals


class  PlayerActionTest:
  var action : TurnAction = _
  var cityList: List[String] = List("Milan", "Rome")

  var value: List[String] = List("Milan", "Rome")

  @Test
  def executeInfectActionTest(): Unit =
    action = InfectAction(cityList)
    val result = action.execute
    assertEquals("InfectAction on " + value, result)

  @Test
  def executeSabotageActionTest() : Unit =
    action = SabotageAction(cityList)
    val result = action.execute
    assertEquals("SabotageAction on " + value, result)

  @Test
  def executeEvolveActionTest() : Unit =
    action = EvolveAction()
    val result = action.execute
    assertEquals("Evolving", result)



