package model

import model.map.WorldMapModule
import model.map.WorldMapModule.{UndeterministicMapModule, WorldMap, createWorldMap}
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.junit.Test

class WorldMapTest:

  var worldMap: WorldMap = createWorldMap(5)(UndeterministicMapModule)

  @Test
  def GetCityTest(): Unit =
    println(worldMap.renderMap())
    assertTrue(worldMap.targetCity("A").getName=="A")
    assertFalse(worldMap.targetCity("A")!=worldMap.targetCity("B"))
    val maybeCityAndCoord = worldMap.findInMap((city,coords) => coords.contains(1,1))
    print(maybeCityAndCoord.get)

