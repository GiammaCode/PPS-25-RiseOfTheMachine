package model

import model.map.WorldMapModule
import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import org.junit.Assert.{assertEquals, assertNotEquals}
import org.junit.Test


class WorldMapTest:

  var worldMap: WorldMap = createWorldMap(10)(DeterministicMapModule)

  @Test
  def CreationOfDeterministicMap(): Unit =
    assertEquals( createWorldMap(10)(DeterministicMapModule), createWorldMap(10)(DeterministicMapModule))


  @Test
  def CreationOfUndeterministicMap(): Unit =
     assertNotEquals( createWorldMap(10)(UndeterministicMapModule), createWorldMap(10)(UndeterministicMapModule))

  @Test
  def changeACityOfTheMap(): Unit =
    assertEquals(worldMap.getCityByName("m").get.infectCity(),worldMap.changeACityOfTheMap(worldMap.getCityByName("m").get.infectCity()).getCityByName("m").get)

  @Test
  def numberOfCityTest():Unit =
    assertEquals(15,worldMap.numberOfCity())

  @Test
  def getAdjacentCities(): Unit =
    assertEquals(worldMap.getAdjacentCities, worldMap.HumanCities)
    val newWorldMAp = worldMap.changeACityOfTheMap(worldMap.getCityByName("m").get.infectCity())
    assertNotEquals(newWorldMAp.getAdjacentCities,newWorldMAp.HumanCities)
    assertEquals(Set("m"), newWorldMAp.AiCities.map(_.getName))


