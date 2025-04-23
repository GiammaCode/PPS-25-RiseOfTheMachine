package view

import org.junit.Test
import org.junit.*
import org.junit.Assert.{assertEquals, assertTrue}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

/** Example of layout
 * -----RISE OF THE MACHINE - TURN 5-----
 *
 * Infected city: 3/15 --> 20.0%
 * Abilities unlocked: ab1,ab2
 *
 * A B C
 * A B C
 * B B C
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
  def renderMapTest(): Unit =
    val testMatrix = Set(
      ("A", Set((0, 0), (1, 0))),
      ("B", Set((2, 0), (0, 1), (1, 1), (2, 1))),
      ("C", Set((0, 2), (1, 2), (2, 2)))
    )
    val output = view.renderMap(testMatrix, 3, 3)

    assertTrue(output.contains("A A B"))
    assertTrue(output.contains("B B B"))
    assertTrue(output.contains("C C C"))

  @Test
  def renderActionMenuTest(): Unit =
    val options = List("Infect", "Sabotages", "Exit")
    val result = view.renderActionMenu(options)
    assertEquals("\nSelect your action:" +
      "\n1. Infect" +
      "\n2. Sabotages" +
      "\n3. Exit" +
      "\nInsert your action > ", result)

