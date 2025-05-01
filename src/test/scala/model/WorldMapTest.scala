package model

import model.map.WorldMapModule
import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import org.junit.Assert.{assertEquals, assertFalse, assertNotEquals, assertTrue}
import org.junit.Test

class WorldMapTest:

  var worldMap: WorldMap = createWorldMap(10)(UndeterministicMapModule)

  @Test
  def CreationOfDeterministicMap(): Unit =
    assertEquals( createWorldMap(10)(DeterministicMapModule), createWorldMap(10)(DeterministicMapModule))


  @Test
  def CreationOfUndeterministicMap(): Unit =
     assertNotEquals( createWorldMap(10)(UndeterministicMapModule), createWorldMap(10)(UndeterministicMapModule))

  @Test
  def changeACityOfTheMap(): Unit =
    assertEquals(worldMap.targetCity("A").infectCity(),worldMap.changeACityOfTheMap(worldMap.targetCity("A").infectCity()).targetCity("A"))

  @Test
  def renderCoordinate(): Unit = println(worldMap.renderList())

  @Test
  def numberOfCityTest():Unit =
    assertEquals(10,createWorldMap(10)(DeterministicMapModule).numberOfCity())


