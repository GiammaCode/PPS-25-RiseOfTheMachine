package view

import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import org.junit.Test
import org.junit.*
import org.junit.Assert.{assertEquals, assertTrue}
import view.ViewModule.*

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
  @Test
  def renderGameTurnTest(): Unit =
    val turn = 5
    val options = List("Infect", "Sabotages", "Exit")
    val abilities = List("ability1", "ability2")
    val worldMap: WorldMap = createWorldMap(5)(DeterministicMapModule)

    CLIView.renderGameTurn(turn, worldMap, 5, 15, abilities, options)
    assertTrue(true)

  /*
  @Test
  def renderTurnTest(): Unit =
    val turn = 5
    CLIView.renderTurn(turn)
    assertTrue(true)

  @Test
  def renderStatusTest(): Unit =
    CLIView.renderStatus(3, 15, List("ability1", "ability2"))
    assertTrue(true)

  @Test
  def renderMapTest(): Unit =
    var worldMap: WorldMap = createWorldMap(5)(DeterministicMapModule)
    CLIView.renderMap(worldMap)
    assertTrue(true)

  @Test
  def renderActionMenuTest(): Unit =
    val options = List("Infect", "Sabotages", "Exit")
    CLIView.renderActionMenu(options)
    assertTrue(true)
   */


