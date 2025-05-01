package model


import model.strategy.*
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals


class  PlayerActionTest:
  var action : TurnAction = _
  var cityList: List[String] = List("Milan", "Rome")

  var value: List[String] = List("Milan", "Rome")

  @Test
  def executeInfectActionTest(): Unit =
    action = Infect(cityList)
    val result = action.execute
    assertEquals("InfectAction on " + value, result)

  @Test
  def executeSabotageActionTest() : Unit =
    action = Sabotage(cityList)
    val result = action.execute
    assertEquals("SabotageAction on " + value, result)

  @Test
  def executeEvolveActionTest() : Unit =
    action = Evolve
    val result = action.execute
    assertEquals("Evolving", result)



