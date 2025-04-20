
import model.MenageCity.BasicCity.{City, createCity}
import model.MenageCity.{BasicCity, Owner}
import org.junit.*
import org.junit.Assert.*
class BasicCityTest:
  var city: City = createCity("A", 3)
  @Test
  def CreateMethodBasicCityTest():Unit =
    assertEquals(city, createCity("A",3))
  @Test
  def GetMethodBasicCityTest(): Unit =
    assertEquals( "A", city.getName)
    assertEquals( Owner.HUMAN, city.getOwner)

  @Test
  def AIInfectsACityTest(): Unit =
    city = city.conqueredCity()
    assertEquals(city.getOwner, Owner.AI)
    city = city.conqueredCity()
    city = city.conqueredCity()
    assertEquals(city.getOwner, Owner.AI)




