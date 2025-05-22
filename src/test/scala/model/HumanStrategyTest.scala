package model

import model.strategy.*
import model.strategy.HumanAction.*
import model.map.WorldState.*
import model.map.WorldMapModule.{UndeterministicMapModule, createWorldMap}
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.junit.*
import org.junit.Assert.*

class SmartHumanStrategyTest:

  var state: WorldState = _

  given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)

  @Before
  def init(): Unit =
    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(5)
    state = createWorldState(map, ai, human, 0)

  @Test
  def testDecideActionReturnsValidAction(): Unit =
    val action = SmartHumanStrategy.decideAction(state)
    println(action.name)
    assertNotNull(action)
    assertTrue(action.isInstanceOf[HumanAction])


