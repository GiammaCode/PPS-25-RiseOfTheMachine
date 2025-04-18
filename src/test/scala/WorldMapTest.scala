import model.WorldMap
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.junit.Test

class WorldMapTest:

  var worldMap: WorldMap = WorldMap(10)

  @Test
  def GetCityTest(): Unit =
    assertTrue(worldMap.targetCity("A").getName=="A")
    assertFalse(worldMap.targetCity("A")!=worldMap.targetCity("B"))

