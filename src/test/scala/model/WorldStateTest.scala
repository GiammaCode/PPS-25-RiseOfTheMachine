package model

import model.map.WorldMapModule.{UndeterministicMapModule, WorldMap, createWorldMap}
import model.map.WorldState.createWorldState
import model.strategy.PlayerAI.*
import model.strategy.{PlayerAI, PlayerEntity, PlayerHuman}
import model.strategy.PlayerHuman.*
import org.junit.*
import org.junit.Assert.{assertEquals, assertFalse, assertNotNull, assertTrue}

class WorldStateTest :
  var human: PlayerHuman = _
  var ai: PlayerAI = _
  var worldMap: WorldMap = _

  @Before
  def init(): Unit =
    human = PlayerHuman.default
    ai = PlayerAI.default
    worldMap = createWorldMap(5)(UndeterministicMapModule)

  @Test
  def testWorldStateCreation(): Unit =
    val state = createWorldState(worldMap, ai, human)
    assertNotNull(state)

  @Test
  def testConqueredCitiesAccess(): Unit =
    val state = createWorldState(worldMap, ai, human)
    assertEquals(Set.empty, state.AIConqueredCities)
    assertEquals(Set("A", "B", "c"), state.humanConqueredCities)

  @Test
  def testAttackableCities(): Unit =
    val state = createWorldState(worldMap, ai, human)
    val attackables = state.attackableCities
    println(attackables)
    assertTrue(attackables.nonEmpty)
    assertTrue(attackables.forall { case (_, inf, sab) =>
      inf >= 0 && sab >= 0
    })

  @Test
  def testIsGameOverReturnsFalseInitially(): Unit =
    val state = createWorldState(worldMap, ai, human)
    assertFalse(state.isGameOver)


