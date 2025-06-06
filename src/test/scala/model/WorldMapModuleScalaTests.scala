package model

import model.map.CityModule.*
import model.map.CityModule.CityImpl.*
import model.map.WorldMapModule.{DeterministicMapModule, WorldMap, createWorldMap}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class WorldMapModuleScalaTests extends AnyFunSuite with Matchers:
  val mapSize = 5

  val map: WorldMap = createWorldMap(mapSize)(using DeterministicMapModule)

  test("create deterministic world map with expected size"):
    map.getSizeOfTheMap shouldBe mapSize

  test("numberOfCity returns the correct number of cities"):
    map.numberOfCity() should be > 0

  test("infecting a city increases numberOfCityInfected"):
    val firstHumanCity = map.humanCities.head
    val infectedCity = firstHumanCity.infectCity()
    val updatedMap = map.changeACityOfTheMap(infectedCity)
    updatedMap.numberOfCityInfected() shouldBe 1

  test("getCityByName should return the correct city"):
    val city = map.humanCities.head
    val found = map.getCityByName(city.getName)
    found shouldBe defined
    found.get.getName shouldBe city.getName

  test("changeACityOfTheMap replaces the city while keeping tiles"):
    val originalCity = map.humanCities.head
    val updatedCity = originalCity.infectCity()
    val newMap = map.changeACityOfTheMap(updatedCity)
    newMap.getCityByName(originalCity.getName).get.getOwner shouldBe Owner.AI

  test("getAdjacentCities should find human cities next to infected"):
    val aiCity = map.humanCities.head.infectCity()
    val updatedMap = map.changeACityOfTheMap(aiCity)
    val adjacents = updatedMap.getAdjacentCities
    adjacents should not be empty


