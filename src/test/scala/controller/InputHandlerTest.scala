package controller

import controller.InputHandling.InvalidChoice
import model.strategy.AiAction
import model.strategy.*
import org.junit.Assert.*
import org.junit.{Before, Test}

class InputHandlerTest :
  val actions : List[AiAction] = List(Sabotage(), Infect(), Evolve)
  val userChoice = 1
  val attackableCities = Set("Rome", "Paris", "Berlin")
  val cityChoice = "Rome"

  @Test
  def testGetActionFromChoiceValid(): Unit =
    val result = InputHandler.getAiActionFromChoice(userChoice, cityChoice, attackableCities, actions)
    val action = result.getOrElse(throw new AssertionError("Expected valid action, but got error."))
    action match {
      case action: Infect =>
        assertEquals("The action should be 'Infect'.", Infect(List("Rome")), action)
      case _ =>
        fail("Expected 'InfectAction'.")
    }
  
  @Test
  def testGetActionFromChoiceInvalidChoice(): Unit =
    val result = InputHandler.getAiActionFromChoice(5, cityChoice, attackableCities, actions)
    val error = result.left.getOrElse(throw new AssertionError("Expected an error due to invalid choice."))
    error match {
      case error: InvalidChoice =>
        assertEquals("The invalid choice should be 5.", 5, error.choice)
        assertEquals("The available range should be 0 to 2.", Range(0, 3), error.availableRange)
      case _ =>
        fail("Expected InvalidChoice error.")
    }