package view

import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy.{PlayerAI, PlayerHuman}
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.junit.Assert.{assertEquals, assertTrue}
import org.junit.*
import view.ViewModule.*

class CLIViewTest:
  var human: PlayerHuman = _
  var ai: PlayerAI = _
  var worldMap: WorldMap = _
  var state: WorldState = _

  given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)

  given GameMode = GameMode.Singleplayer

  @Before
  def init(): Unit =
    human = PlayerHuman.fromSettings
    ai = PlayerAI.fromSettings
    worldMap = createWorldMap(5)
    state = createWorldState(worldMap, ai, human, 0)

  @Test
  def testRenderGameModeMenu_SinglePlayer_Easy(): Unit =
    val input = new java.io.ByteArrayInputStream("1\n1\n".getBytes) // 1 → Singleplayer, 1 → Easy
    val outputBuffer = new java.io.ByteArrayOutputStream()

    val gameSettings = Console.withIn(input) {
      Console.withOut(outputBuffer) {
        CLIView.renderGameModeMenu()
      }
    }

    assertEquals(GameMode.Singleplayer, gameSettings.gameMode)
    assertEquals(Difficulty.Easy, gameSettings.difficulty)

  @Test
  def testRenderGameModeMenu_InvalidDefaults(): Unit =
    val input = new java.io.ByteArrayInputStream("wrong\nx\n".getBytes)
    val outputBuffer = new java.io.ByteArrayOutputStream()

    val gameSettings = Console.withIn(input) {
      Console.withOut(outputBuffer) {
        CLIView.renderGameModeMenu()
      }
    }

    assertEquals(GameMode.Singleplayer, gameSettings.gameMode)
    assertEquals(Difficulty.Normal, gameSettings.difficulty)


  @Test
  def testRenderGameModeMenu_Multiplayer_DefaultsToNormal(): Unit =
    val input = new java.io.ByteArrayInputStream("2\n".getBytes) // 2 → Multiplayer
    val outputBuffer = new java.io.ByteArrayOutputStream()

    val gameSettings = Console.withIn(input) {
      Console.withOut(outputBuffer) {
        CLIView.renderGameModeMenu()
      }
    }

    assertEquals(GameMode.Multiplayer, gameSettings.gameMode)
    assertEquals(Difficulty.Normal, gameSettings.difficulty)


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

    assertTrue(printedOutput.contains("RISE OF THE MACHINE"))
    assertTrue(printedOutput.contains("Infected Cities:"))
    assertTrue(printedOutput.contains("Insert your action"))
    assertEquals(((1, "A"), None), result)



