package model

import model.strategy.*
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PlayerActionTest:
  var action: TurnAction = _
  var cityList: List[String] = List("A", "B")

  // Funzione di supporto per catturare l'output di println
  private def captureOutput(block: => Unit): String =
    val outCapture = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outCapture)) {
      block
    }
    outCapture.toString.trim

  @Test
  def executeInfectActionTest(): Unit =
    action = Infect(cityList)
    val output = captureOutput {
      action.execute
    }
    assertEquals("AI Infect succeeded: Infected A, B", output)

  @Test
  def executeSabotageActionTest(): Unit =
    action = Sabotage(cityList)
    val output = captureOutput {
      action.execute
    }
    assertEquals("AI Sabotage succeeded: Sabotaged A, B", output)

  @Test
  def executeEvolveActionTest(): Unit =
    action = Evolve
    val output = captureOutput {
      action.execute
    }
    assertEquals("AI Evolution complete", output)
