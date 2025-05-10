package controller

import model.strategy.{AiAction, Evolve, Infect, Sabotage}
import org.junit.Assert.*
import org.junit.Test

class GameControllerTest :
  @Test
  def testUserInputActionSelection(): Unit =
    val actions = List(Sabotage(), Infect(), Evolve)
    val userChoice = 1
    val attackableCities = Set("Rome", "Paris", "Berlin")
    val result = InputHandler.getActionFromChoice(userChoice,InputHandler.CityContext("Rome", attackableCities), actions)
    assertTrue("Expected valid action, but got error.", result.isRight)
    result.getOrElse(throw new AssertionError("Expected an action, but got error.")) match {
      case action: Infect =>
        assertEquals("Expected action should be InfectAction", Infect(List("Rome")), action)
      case _ => fail("Expected InfectAction, but got different action.")
    }
