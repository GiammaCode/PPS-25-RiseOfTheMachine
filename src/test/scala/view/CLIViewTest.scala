package view

import org.junit.Test
import org.junit.*
import org.junit.Assert.{assertEquals, assertTrue}

import java.io.{ByteArrayOutputStream, PrintStream}

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

Seleziona la tua azione:
1. Infetta città adiacente
3. Sabota città adiacente
4. Evolvi nuova capacità
5. Auto-turno (AI decide)
0. Esci

Inserisci il numero dell'azione > _
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
