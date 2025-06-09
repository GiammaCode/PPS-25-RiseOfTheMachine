package model

import model.map.WorldMapModule.createWorldMap
import model.map.WorldState.*
import model.strategy.*
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.junit.*
import org.junit.Assert.*

class PlayerHumanTest:

  given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)

  val MapSize = 10
  var player: PlayerHuman = _
  var worldState: WorldState = _

  @Before
  def init(): Unit =
    player = PlayerHuman.fromSettings
    worldState = createWorldState( createWorldMap(MapSize), PlayerAI.fromStats, PlayerHuman.fromStats, 0)

  @Test
  def developKillSwitchIncreasesProgress(): Unit =
    val updatedPlayer = player.executeAction(DevelopKillSwitch, worldState.worldMap).getPlayer
    //killswitch with normal difficulty starts to 5 (+10)
    assertEquals(15, updatedPlayer.killSwitch)
    assertEquals(1, updatedPlayer.executedActions.size)

  @Test
  def applyCityDefenseAction(): Unit =
    val city: List[String] = List("A")
    val updatedPlayer = player.executeAction(CityDefense(city), worldState.worldMap).getPlayer
    assertTrue(updatedPlayer.defendedCities.contains("A"))
    assertEquals(1, updatedPlayer.defendedCities.size)
    assertEquals(1, updatedPlayer.executedActions.size)

  @Test
  def applyGlobalAction(): Unit =
    val cities: List[String] = List("A", "B")
    val updatedPlayer = player.executeAction(GlobalDefense(cities), worldState.worldMap).getPlayer
    assertEquals(1, updatedPlayer.executedActions.size)