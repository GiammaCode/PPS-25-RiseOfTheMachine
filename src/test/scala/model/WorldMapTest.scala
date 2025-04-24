package model

import model.map.WorldMapModule
import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import org.junit.Assert.{assertEquals, assertFalse, assertNotEquals, assertTrue}
import org.junit.Test

class WorldMapTest:

  var worldMap: WorldMap = createWorldMap(5)(UndeterministicMapModule)

  @Test
  def GetCityTest(): Unit =
    assertTrue(worldMap.targetCity("City1").getName=="City1")
    assertTrue(worldMap.targetCity("City1")!=worldMap.targetCity("City2"))

  @Test
  def CreationOfDeterministicMap(): Unit =
    assertEquals( createWorldMap(10)(DeterministicMapModule), createWorldMap(10)(DeterministicMapModule))


  @Test
  def CreationOfUndeterministicMap(): Unit =
     assertNotEquals( createWorldMap(10)(UndeterministicMapModule), createWorldMap(10)(UndeterministicMapModule))





