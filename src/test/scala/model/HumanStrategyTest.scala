package model

import model.strategy.*
import model.strategy.HumanAction.*
import model.map.WorldState.*
import model.map.WorldMapModule.{createWorldMap, UndeterministicMapModule}
import model.util.GameDifficulty.Difficulty.Normal
import org.junit.*
import org.junit.Assert.*

class SmartHumanStrategyTest:

  var state: WorldState = _

  @Before
  def init(): Unit =
    val player = PlayerHuman.fromDifficulty(Normal)
    val ai = PlayerAI.fromDifficulty(Normal)
    val map = createWorldMap(5)(UndeterministicMapModule)
    state = createWorldState(map, ai, player)

  @Test
  def testDecideActionReturnsValidAction(): Unit =
    val action = SmartHumanStrategy.decideAction(state)
    println(action.name)
    assertNotNull(action)
    assertTrue(action.isInstanceOf[HumanAction])


