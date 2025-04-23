package view

import org.junit.Test
import org.junit.*
import org.junit.Assert.{assertEquals, assertTrue}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

/**
## Esempio di turno
====== RISE OF THE MACHINE - TURNO 3 ======

Stato attuale:
- Citta infette: 5/15 (33%)
- Abilità sbloccate: [upgrade1, upgrade2]

[Mappa ASCII con colori ANSI]
0 1 2 3 4 5 6 7 8 9
0  S S S S S S S S S S
1  S S S D S S S S S S
2  S I I C S S S S S S
3  S I S S S S S S S S
4  S S S S S S S S S S

Select your action:
1. Infect adjacent city
2. Sabotages adjacent city
3. Evolve new capacity
4. Auto-shift
0. Exit

Insert your action > _
 */

class CLIViewTest:

  def captureStdOut(block: => Unit): String =
    val originalOut = System.out
    val baos = new ByteArrayOutputStream()
    val newOut = new PrintStream(baos)
    System.setOut(newOut)

    try
      block
    finally
      System.setOut(originalOut)

    baos.toString.trim

  var view: CLIView = _

  @Before
  def initialize(): Unit = {
    view = CLIView()
  }

  @Test
  def showTurnTest(): Unit =
    val turn = 5
    val output = captureStdOut(
      view.showTurn(turn)
    )
    assertTrue(output.contains("TURN 5"))

  @Test
  def showStatusTest() : Unit =
    val output = captureStdOut {
      view.showStatus(5, 15, List("ability1", "ability2"))
    }
    assertTrue(output.contains("Infected city: 5/15 (33%)"))
    assertTrue(output.contains("ability1, ability2"))

  /*
  Test matrix layout
  A B C
  A B C
  B B C
  * */
  @Test
  def showMapTest() : Unit =
    val testMatrix  = Set(
      ("A", Set((0,0),(1,0))),
      ("B", Set((2,0), (0,1), (1,1), (2,1))),
      ("C", Set((0,2), (1,2), (2,2)))
    )
    val output = captureStdOut{
      view.showMap(testMatrix, 3, 3)
    }
    assertTrue(output.contains("A B C"))
    assertTrue(output.contains("A B C"))
    assertTrue(output.contains("B B C"))

  @Test
  def askActionTest: Unit =
    val fakeInput = new ByteArrayInputStream("2\n".getBytes)
    val originalIn = System.in
    val options = List("Infect", "Sabotages", "Exit")

    System.setIn(fakeInput)
    val result = view.askAction(options)
    assertEquals(2, result)
    System.setIn(originalIn)

