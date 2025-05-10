package model

import model.strategy.*
import org.junit.Test
import org.junit.*
import org.junit.Assert.assertEquals
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PlayerActionTest:

  @Test
  def executeInfectActionTest(): Unit =
    val targets = List("CityA", "CityB", "CityC")
    val infectAction = Infect(targets)
    assertEquals(targets, infectAction.targets)

  @Test
  def executeSabotageActionTest(): Unit =
    val targets = List("Base1")
    val sabotageAction = Sabotage(targets)
    assertEquals(targets, sabotageAction.targets)

  @Test
  def executeEvolveActionTest(): Unit =
    val evolveAction = Evolve
    assertEquals(List.empty, evolveAction.targets)