package model

import model.strategy.*
import model.strategy.HumanAction.*
import model.map.WorldState.*
import model.map.WorldMapModule.{UndeterministicMapModule, createWorldMap}
import .Difficulty
import .Difficulty.Normal
import org.junit.*
import org.junit.Assert.*

class SmartHumanStrategyTest:

  var state: WorldState = _

  given Difficulty = Difficulty.Easy

  @Before
  def init(): Unit =
    val player = PlayerHuman.fromDifficulty(Normal)
    val ai = PlayerAI.fromDifficulty(Normal)
    val map = createWorldMap(5)
    state = createWorldState(map, ai, player)

  @Test
  def testDecideActionReturnsValidAction(): Unit =
    val action = SmartHumanStrategy.decideAction(state)
    println(action.name)
    assertNotNull(action)
    assertTrue(action.isInstanceOf[HumanAction])


