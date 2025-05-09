package controller

import controller.InputHandler.*
import controller.InputHandling.*
import controller.InputHandler.given

import model.strategy.*
import org.junit.Assert.*
import org.junit.Test

class InputHandlerTest:

  val aiActions: List[AiAction] = List(Sabotage(), Infect(), Evolve)
  val humanActions: List[HumanAction] = List(CityDefense(Nil), GlobalDefense(Nil), DevelopKillSwitch)

  val attackableCities = Set("Rome", "Paris", "Berlin")
  val cityChoice = "Rome"
  val aiContext = CityContext(cityChoice, attackableCities)

  val targets = List(("Rome"),("Paris"))
  val targetContext = TargetContext(targets)

  /** --- AI ACTION TESTS --- */

  @Test
  def testGetAiActionFromChoiceValid(): Unit =
    val result = InputHandler.getActionFromChoice(1, aiContext, aiActions)
    val action = result.getOrElse(fail("Expected valid action, got error."))

    action match
      case Infect(cities) => assertEquals(List("Rome"), cities)
      case _ => fail("Expected Infect action.")

  @Test
  def testGetAiActionFromChoiceInvalidIndex(): Unit =

    val result = InputHandler.getActionFromChoice(5, aiContext, aiActions)
    val error = result.left.getOrElse(fail("Expected error for invalid choice."))

    error match
      case InvalidChoice(choice, range) =>
        assertEquals(5, choice)
        assertEquals(0 to 2, range)
      case _ => fail("Expected InvalidChoice.")

  @Test
  def testGetAiActionFromChoiceInvalidCity(): Unit =
    val invalidContext = CityContext("Madrid", attackableCities) // Madrid is not attackable
    val result = InputHandler.getActionFromChoice(0, invalidContext, aiActions)

    val error = result.left.getOrElse(fail("Expected InputParsingError."))
    error match
      case InputParsingError(input, msg) =>
        assertEquals("Madrid", input)
        assertTrue(msg.contains("not attackable"))
      case _ => fail("Expected InputParsingError.")

  /** --- HUMAN ACTION TESTS --- */

  @Test
  def testGetHumanActionFromChoiceValid(): Unit =
    given ActionResolver[HumanAction] = humanActionResolver

    val result = InputHandler.getActionFromChoice(1, targetContext, humanActions)
    val action = result.getOrElse(fail("Expected valid human action."))

    action match
      case GlobalDefense(t) => assertEquals(targets, t)
      case _ => fail("Expected GlobalDefense.")

  @Test
  def testGetHumanActionFromChoiceNoTargets(): Unit =
    given ActionResolver[HumanAction] = humanActionResolver

    val emptyContext = TargetContext(Nil)
    val result = InputHandler.getActionFromChoice(0, emptyContext, humanActions)

    val error = result.left.getOrElse(fail("Expected InputParsingError for empty targets."))
    error match
      case InputParsingError(input, msg) =>
        assertEquals("Targets", input)
        assertTrue(msg.contains("No targets"))
      case _ => fail("Expected InputParsingError.")
