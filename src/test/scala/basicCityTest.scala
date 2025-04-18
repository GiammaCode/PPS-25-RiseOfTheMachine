import model.basicCity
import model.city.Owner
import org.junit.*
import org.junit.Assert.*
class basicCityTest:
  val city: basicCity = basicCity("A", Owner.HUMAN, 3)

  @Test
  def GetMethodBasicCityTest(city: basicCity): Unit =
    assertTrue(city.getSize == 3)
    assertTrue(city.getName == "A")
    assertTrue(city.getOwner== Owner.HUMAN)

  @Test
  def AIInfectACityTest(city: basicCity): Unit =
    city.changeOwner(Owner.AI)
    assertTrue(city.getOwner == Owner.AI)




