package model

import model.strategy.HumanAction.*
import model.strategy.*
import org.junit.*
import org.junit.Assert.*

class HumanActionTest:

  val targets = List("CityA", "CityB")

  @Test
  def cityDefenseShouldStoreTargets(): Unit =
    val action = CityDefense(targets)
    assertEquals(targets, action.targets)
    assertEquals("CityDefense", action.name)

  @Test
  def globalDefenseShouldStoreTargets(): Unit =
    val action = GlobalDefense(targets)
    assertEquals(targets, action.targets)
    assertEquals("GlobalDefense", action.name)

  @Test
  def developKillSwitchShouldHaveNoTargets(): Unit =
    val action = DevelopKillSwitch
    assertTrue(action.targets.isEmpty)
    assertEquals("DevelopKillSwitch", action.name)

  @Test
  def allActionsShouldContainAllThree(): Unit =
    val names = allActions.map(_.name)
    assertTrue(names.contains("CityDefense"))
    assertTrue(names.contains("GlobalDefense"))
    assertTrue(names.contains("DevelopKillSwitch"))
    assertEquals(3, allActions.size)
