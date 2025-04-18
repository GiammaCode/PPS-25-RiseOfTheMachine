import model.MenageCity.*
import model.WorldMap
import org.junit.*
import org.junit.Assert.*
class BasicCityTest:
  val city: City = City("A", 3)

  @Test
  def GetMethodBasicCityTest(): Unit =
    assertTrue(city.getSize == 3)
    assertTrue(city.getName == "A")
    assertTrue(city.getOwner== Owner.HUMAN)

  @Test
  def AIInfectsACityTest(): Unit =
    city.conqueredCity()
    assertTrue(city.getOwner == Owner.AI)




