package view

import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy.{PlayerAI, PlayerHuman}
import model.util.GameDifficulty.Difficulty.Normal
import org.junit.Assert.{assertEquals, assertTrue}
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
    human = PlayerHuman.fromDifficulty(Normal)
    ai = PlayerAI.fromDifficulty(Normal)
    worldMap = createWorldMap(5)(UndeterministicMapModule)
    state = createWorldState(worldMap, ai, human)

  @Test
  def testRenderGameTurnWithSimulatedInput(): Unit =
    val simulatedInput = new java.io.ByteArrayInputStream("1 A\n".getBytes)
    val outputBuffer = new java.io.ByteArrayOutputStream()

    val result = Console.withIn(simulatedInput) {
      Console.withOut(outputBuffer) {
        CLIView.renderGameTurn(state)
      }
    }

    val printedOutput = outputBuffer.toString

    assertTrue(printedOutput.contains("RISE OF THE MACHINE - TURN"))
    assertTrue(printedOutput.contains("Infected city:"))
    assertTrue(printedOutput.contains("Insert your action"))
    assertEquals((1, "A"), result)



