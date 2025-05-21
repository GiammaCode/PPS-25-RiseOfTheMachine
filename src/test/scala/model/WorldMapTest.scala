package model

import model.map.WorldMapModule
import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import org.junit.Assert.{assertEquals, assertNotEquals}
import org.junit.Test

import WorldMapModule.given
class WorldMapTest:

  var worldMap: WorldMap = createWorldMap(10)(using DeterministicMapModule)

  @Test
  def creationOfDeterministicMap(): Unit =
    assertEquals( createWorldMap(10)(using DeterministicMapModule), createWorldMap(10)(using DeterministicMapModule))


  @Test
  def creationOfUndeterministicMap(): Unit =
     assertNotEquals( createWorldMap(10), createWorldMap(10))

  @Test
  def numberOfCityTest():Unit =
    assertEquals(15,worldMap.numberOfCity())

  @Test
  def getCityByNameTest(): Unit =
    assertEquals("i",worldMap.getCityByName("i").get.getName)

  @Test
  def getNumberoOfInfectedCity: Unit =
    assertEquals(0,worldMap.numberOfCityInfected())
    assertEquals(1,worldMap.changeACityOfTheMap(worldMap.getCityByName("i").get.infectCity()).numberOfCityInfected())
  @Test
  def findInWorldMapTest() : Unit =
     assertEquals(Some("j"), worldMap.findInMap { case (_, coords) => coords.contains(2,2)})
  @Test
  def getAdjacentCities(): Unit =
    assertEquals(worldMap.getAdjacentCities, worldMap.HumanCities)
    val newWorldMAp = worldMap.changeACityOfTheMap(worldMap.getCityByName("m").get.infectCity())
    assertNotEquals(newWorldMAp.getAdjacentCities,newWorldMAp.HumanCities)
    assertEquals(Set("m"), newWorldMAp.AiCities.map(_.getName))

  @Test
  def changeACityOfTheMap(): Unit =
      assertEquals(worldMap.getCityByName("i").get.infectCity(), worldMap.changeACityOfTheMap(worldMap.getCityByName("i").get.infectCity()).getCityByName("i").get)
      assertNotEquals(worldMap.getCityByName("i").get.infectCity(), worldMap.changeACityOfTheMap(worldMap.getCityByName("j").get.infectCity()).getCityByName("j").get)




