package model

import model.strategy.{CityDefense, DevelopKillSwitch, GlobalDefense, HumanAction, PlayerHuman}
import org.junit.*
import org.junit.Assert.{assertEquals, assertTrue}
import model.strategy.*
import model.strategy.ExecuteActionResult.*
import model.strategy.HumanAction.*
import org.junit.*
import org.junit.Assert.*

class PlayerHumanTest:

  var player: PlayerHuman = _

  @Before
  def init(): Unit =
    player = PlayerHuman.default

  @Test
  def testInitialState(): Unit =
    assertEquals(0, player.killSwitch)
    assertTrue(player.defendedCities.isEmpty)
    assertTrue(player.executedActions.isEmpty)
    assertTrue(player.conqueredCities.isEmpty)

  @Test
  def testCityDefenseAction(): Unit =
    val targets = List("CityA")
    val result = player.executeAction(CityDefense(targets))
    val updatedPlayer = result.getPlayer

    assertTrue(updatedPlayer.defendedCities.contains("CityA"))
    assertTrue(updatedPlayer.executedActions.exists(_.isInstanceOf[CityDefense]))

  @Test
  def testGlobalDefenseAction(): Unit =
    val targets = List("CityA", "CityB")
    val result = player.executeAction(GlobalDefense(targets))
    val updatedPlayer = result.getPlayer

    assertTrue(updatedPlayer.defendedCities.contains("CityA"))
    assertTrue(updatedPlayer.defendedCities.contains("CityB"))
    assertTrue(updatedPlayer.executedActions.exists(_.isInstanceOf[GlobalDefense]))

  @Test
  def testKillSwitchAction(): Unit =
    val result = player.executeAction(DevelopKillSwitch)
    val updatedPlayer = result.getPlayer

    assertEquals(1, updatedPlayer.killSwitch)
    assertTrue(updatedPlayer.executedActions.contains(DevelopKillSwitch))
