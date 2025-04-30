package view

import model.map.WorldMapModule.{UndeterministicMapModule, WorldMap, createWorldMap}
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy.PlayerAI.PlayerAI
import model.strategy.{PlayerAI, PlayerHuman}
import model.strategy.PlayerHuman.PlayerHuman
import org.junit.Assert.assertTrue
import org.junit.*
import view.ViewModule.*

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
  var human: PlayerHuman = _
  var ai: PlayerAI = _
  var worldMap: WorldMap = _
  var state: WorldState = _

  @Before
  def init(): Unit =
    human = PlayerHuman.default
    ai = PlayerAI.default
    worldMap = createWorldMap(5)(UndeterministicMapModule)
    state = createWorldState(worldMap, ai, human)

  @Test
  def renderGameTurnTest(): Unit =
    val turn = 5
    val options = List("Infect", "Sabotages", "Exit")
    val abilities = Set("ability1", "ability2")
    val worldMap: WorldMap = createWorldMap(10)(UndeterministicMapModule)


    CLIView.renderGameTurn(state)
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


