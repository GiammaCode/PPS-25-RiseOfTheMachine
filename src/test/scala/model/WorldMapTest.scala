package model

import model.map.WorldMapModule
import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.junit.Test

class WorldMapTest:

  var worldMap: WorldMap = createWorldMap(5)(UndeterministicMapModule)

  @Test
  def GetCityTest(): Unit =
    assertTrue(worldMap.targetCity("A").getName=="A")
    assertFalse(worldMap.targetCity("A")!=worldMap.targetCity("B"))
    val maybeCityAndCoord = worldMap.findInMap((city,coords) => coords.contains(1,1))
    print(maybeCityAndCoord.get)

  @Test
  def CreationOfDeterministicMap(): Unit =
    val worldMap = createWorldMap(10)(DeterministicMapModule).renderNamedCoordinates()


  @Test
  def CreationOfUndeterministicMap(): Unit =
    val map = createWorldMap(10)(UndeterministicMapModule).renderNamedCoordinates()





