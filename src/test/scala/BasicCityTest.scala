import model.MenageCity.*
import org.junit.*
import org.junit.Assert.*
class BasicCityTest:
  val city: City = City("A", 3)

  @Test
  def GetMethodBasicCityTest(city: City): Unit =
    assertTrue(city.getSize == 3)
    assertTrue(city.getName == "A")
    assertTrue(city.getOwner== Owner.HUMAN)

  @Test
  def AIInfectACityTest(city: City): Unit =
    city.changeOwner(Owner.AI)
    assertTrue(city.getOwner == Owner.AI)




