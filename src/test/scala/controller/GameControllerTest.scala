package controller

import model.strategy.playerActions.*
import org.junit.Assert.*
import org.junit.Test

class GameControllerTest :
  @Test
  def testUserInputActionSelection(): Unit = 
    val actions = List(SabotageAction(), InfectAction(), EvolveAction())
    val userChoice = 2
    val attackableCities = Set("Rome", "Paris", "Berlin")
    val result = InputHandler.getActionFromChoice(userChoice, "Rome", attackableCities, actions)
    assertTrue("Expected valid action, but got error.", result.isRight)
    result.getOrElse(throw new AssertionError("Expected an action, but got error.")) match {
      case action: InfectAction =>
        assertEquals("Expected action should be InfectAction", InfectAction(), action)
      case _ => fail("Expected InfectAction, but got different action.")
    }
