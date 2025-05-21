package controller

import controller.InputHandler.*
import controller.InputHandling.*
import controller.InputHandler.given

import model.strategy.*
import org.junit.Assert.*
import org.junit.Test

class InputHandlerTest:

  val aiActions: List[AiAction] = List(Sabotage(), Infect(), Evolve)
  val humanActions: List[HumanAction] = List(CityDefense(), GlobalDefense(), DevelopKillSwitch)

  val validCities = Set("Rome", "Paris", "Berlin")
  val cityChoice = "Rome"
  val aiContext = CityContext(cityChoice, validCities)
  val humanContext = CityContext(cityChoice, validCities)

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
    val invalidContext = CityContext("Madrid", validCities)
    val result = InputHandler.getActionFromChoice(0, invalidContext, aiActions)

    val error = result.left.getOrElse(fail("Expected InputParsingError."))
    error match
      case InputParsingError(input, msg) =>
        assertEquals("Madrid", input)
        assertTrue(msg.contains("not attackable"))
      case _ => fail("Expected InputParsingError.")
