package model

import model.map.WorldMapModule.createWorldMap
import model.map.WorldState.*
import model.strategy.*
import .Difficulty
import .Difficulty.{Easy, Hard, Normal}
import org.junit.*
import org.junit.Assert.*

class PlayerHumanTest:

  given Difficulty = Difficulty.Easy // TODO: get from CLI

  var player: PlayerHuman = _
  var worldState: WorldState = _
  val cities: List[String] = List("A", "B")

  @Before
  def init(): Unit =
    player = PlayerHuman.fromDifficulty(Normal)
    worldState = createWorldState( createWorldMap(10), PlayerAI.fromStats, PlayerHuman.fromStats)

  @Test
  def developKillSwitchIncreasesProgress(): Unit =
    val updatedPlayer = player.executeAction(DevelopKillSwitch, worldState.worldMap).getPlayer
    //killswitch with normal difficulty starts to 30 (+10)
    assertEquals(40, updatedPlayer.killSwitch)
    assertEquals(1, updatedPlayer.executedActions.size)

  @Test
  def applyCityDefenseAction(): Unit =
    val updatedPlayer = player.executeAction(CityDefense(cities), worldState.worldMap).getPlayer
    assertTrue(updatedPlayer.defendedCities.contains("A"))
    assertTrue(updatedPlayer.defendedCities.contains("B"))
    assertEquals(2, updatedPlayer.defendedCities.size)
    assertEquals(1, updatedPlayer.executedActions.size)

  @Test
  def normalDifficultyStatsTest(): Unit =
    val expected = GameDifficulty.humanStatsFor(Normal)
    assertEquals(expected.killSwitch, player.killSwitch)

  @Test
  def easyDifficultyStatsTest(): Unit =
    val easyPlayer = PlayerHuman.fromDifficulty(Easy)
    val expected = GameDifficulty.humanStatsFor(Easy)
    assertEquals(expected.killSwitch, easyPlayer.killSwitch)

  @Test
  def hardDifficultyStatsTest(): Unit =
    val hardPlayer = PlayerHuman.fromDifficulty(Hard)
    val expected = GameDifficulty.humanStatsFor(Hard)
    assertEquals(expected.killSwitch, hardPlayer.killSwitch)
