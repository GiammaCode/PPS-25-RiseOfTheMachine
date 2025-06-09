package model

import model.map.WorldMapModule.{WorldMap, createWorldMap}
import model.map.WorldState.*
import model.strategy.{Evolve, Infect, PlayerAI, PlayerHuman, Sabotage}
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.junit.*
import org.junit.Assert.*

class WorldStateTest:

  var human: PlayerHuman = _
  var ai: PlayerAI = _
  var worldMap: WorldMap = _

  val MapSize: Int = 5
  val SecureEvent: Int = 100

  given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)


  @Before
  def init(): Unit =
    human = PlayerHuman.fromSettings
    ai = PlayerAI.fromSettings
    worldMap = createWorldMap(MapSize)

  @Test
  def testWorldStateCreation(): Unit =
    val state = createWorldState(worldMap, ai, human, 0)
    assertNotNull(state)

  @Test
  def testConqueredCitiesAccess(): Unit =
    val state = createWorldState(worldMap, ai, human, 0)
    assertEquals(Set.empty, state.AIConqueredCities)
    assertEquals(Set.empty, state.humanConqueredCities)

  @Test
  def testAttackableCities(): Unit =
    val state = createWorldState(worldMap, ai, human, 0)
    val attackables = state.attackableCities
    println(attackables)
    assertTrue(attackables.nonEmpty)
    assertTrue(attackables.forall { case (_, inf, sab) =>
      inf >= 0 && sab >= 0
    })

  @Test
  def testIsGameOverInitially(): Unit =
    val state = createWorldState(worldMap, ai, human, 0)
    assertFalse(state.isGameOver._1)

  @Test
  def testProbabilityByCityAndAction(): Unit =
    val state = createWorldState(worldMap, ai, human, 0)
    val attackables = state.attackableCities

    //first city of the list
    val (cityName, expectedInfect, expectedSabotage) = attackables.head
    val infectProbability = state.probabilityByCityandAction(cityName, Infect(List(cityName)))
    println(s"Infect: $infectProbability")
    assertEquals("Infect probability is wrong", expectedInfect, infectProbability)

    val sabotageProbability = state.probabilityByCityandAction(cityName, Sabotage(List(cityName)))
    println(s"Sabotage: $sabotageProbability")
    assertEquals("Sabotage probability is wrong", expectedSabotage, sabotageProbability)

    val evolveProbability = state.probabilityByCityandAction(cityName, Evolve)
    assertEquals( SecureEvent, evolveProbability)
