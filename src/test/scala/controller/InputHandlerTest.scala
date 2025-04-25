package controller

import controller.InputHandling.InvalidChoice
import model.strategy.AiAction
import model.strategy.playerActions.{EvolveAction, InfectAction, SabotageAction}
import org.junit.Assert.*
import org.junit.{Before, Test}

class InputHandlerTest :
  val actions : List[AiAction] = List(SabotageAction(), InfectAction(), EvolveAction())

  // Test: Test for a valid action selection
  @Test
  def testGetActionFromChoiceValid(): Unit =
    val result = InputHandler.getActionFromChoice(2, actions)
    val action = result.getOrElse(throw new AssertionError("Expected valid action, but got error."))
    action match {
      case action: InfectAction =>
        assertEquals("The action should be 'Infect'.", InfectAction(), action)
      case _ =>
        fail("Expected 'InfectAction'.")
    }

  // Test: Test for invalid action selection (choice out of range)
  @Test
  def testGetActionFromChoiceInvalidChoice(): Unit =
    val result = InputHandler.getActionFromChoice(5, actions)
    val error = result.left.getOrElse(throw new AssertionError("Expected an error due to invalid choice."))
    error match {
      case error: InvalidChoice =>
        assertEquals("The invalid choice should be 5.", 5, error.choice)
        assertEquals("The available range should be 1 to 3.", Range(1, 3), error.availableRange)
      case _ =>
        fail("Expected InvalidChoice error.")
    }