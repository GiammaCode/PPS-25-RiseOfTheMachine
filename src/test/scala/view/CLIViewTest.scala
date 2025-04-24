package view

import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, createWorldMap}
import org.junit.Test
import org.junit.*
import org.junit.Assert.{assertEquals, assertTrue}
import view.ViewModule.CLIView

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

/** Example of layout
 * -----RISE OF THE MACHINE - TURN 5-----
 *
 * Infected city: 3/15 --> 20.0%
 * Abilities unlocked: ab1,ab2
 *
 * A A B
 * B B B
 * C C C
 * Select your action:
 * 1. Infect
 * 2. Sabotages
 * 3. Exit
 * Insert your action >
 */

class CLIViewTest:
  var view: CLIView = _

  @Before
  def initialize(): Unit = {
    view = CLIView()
  }

  @Test
  def renderTurnTest(): Unit =
    val turn = 5
    val result = view.renderTurn(turn)
    assertEquals("\n-----RISE OF THE MACHINE - TURN 5-----\n", result)

  @Test
  def renderStatusTest(): Unit =
    val result = view.renderStatus(3, 15, List("ability1", "ability2"))
    assertEquals("Infected city: 3/15 --> 20.0%\nAbilities unlocked: ability1,ability2\n", result)

  @Test
  def RenderDeterministicMapTest(): Unit =
    val map = createWorldMap(10)(DeterministicMapModule)
    val output = view.renderMap(map)
    println(output)

  @Test
  def RenderUndeterministicMapTest(): Unit =
    val map = createWorldMap(10)(UndeterministicMapModule)
    val output = view.renderMap(map)
    println(output)


  @Test
  def renderActionMenuTest(): Unit =
    val options = List("Infect", "Sabotages", "Exit")
    val result = view.renderActionMenu(options)
    assertEquals("\nSelect your action:" +
      "\n1. Infect" +
      "\n2. Sabotages" +
      "\n3. Exit" +
      "\nInsert your action > ", result)


