package model

import model.map.WorldMapModule
import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import org.junit.Assert.{assertEquals, assertNotEquals}
import org.junit.Test

import WorldMapModule.given
class WorldMapTest:

  private val mapSize = 10

  private val iCityName = "i"

  val mCityName = "m"

  val ACapitalName = "A"

  private val jCityName = "j"

  private val inGridCoord = (2, 2)

  var worldMap: WorldMap = createWorldMap(mapSize)(using DeterministicMapModule)

  @Test
  def creationOfDeterministicMap(): Unit =
    assertEquals( createWorldMap(mapSize)(using DeterministicMapModule), createWorldMap(mapSize)(using DeterministicMapModule))


  @Test
  def creationOfUndeterministicMap(): Unit =
     assertNotEquals( createWorldMap(mapSize), createWorldMap(mapSize))

  @Test
  def numberOfCityTest():Unit = {
    val expectedDeterministicCity = 14
    assertEquals(expectedDeterministicCity, worldMap.numberOfCity())
  }

  @Test
  def getCityByNameTest(): Unit =
    assertEquals(iCityName,worldMap.getCityByName(iCityName).get.getName)

  @Test
  def getNumberoOfInfectedCity: Unit =
    assertEquals(0,worldMap.numberOfCityInfected())
    assertEquals(1,worldMap.changeACityOfTheMap(worldMap.getCityByName(iCityName).get.infectCity()).numberOfCityInfected())


  @Test
  def findInWorldMapTest() : Unit =
     assertEquals(Some("g"), worldMap.findInMap { case (_, coords) => coords.contains(inGridCoord)})
  @Test
  def getAdjacentCities(): Unit =
    assertEquals(worldMap.getAdjacentCities, worldMap.humanCities)
    val newWorldMAp = worldMap.changeACityOfTheMap(worldMap.getCityByName(mCityName).get.infectCity())
    assertNotEquals(newWorldMAp.getAdjacentCities,newWorldMAp.humanCities)
    assertEquals(Set(mCityName), newWorldMAp.aiCities.map(_.getName))

  @Test
  def changeACityOfTheMap(): Unit =
      assertEquals(
        worldMap.getCityByName(iCityName).get.infectCity(),
        worldMap.changeACityOfTheMap(worldMap.getCityByName(iCityName).get.infectCity()).getCityByName(iCityName).get
      )

      assertNotEquals(
        worldMap.getCityByName(iCityName).get.infectCity(),
        worldMap.changeACityOfTheMap(worldMap.getCityByName(jCityName).get.infectCity()).getCityByName(jCityName).get
      )

  @Test
  def conquerCapitalTest(): Unit =
    assertEquals(0,worldMap.capitalConqueredCounter)
    assertEquals(1,worldMap.changeACityOfTheMap(worldMap.getCityByName(ACapitalName).get.infectCity()).capitalConqueredCounter)




