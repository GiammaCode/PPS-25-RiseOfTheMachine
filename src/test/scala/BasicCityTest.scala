
import model.MenageCity.{BasicCity, CityModule, Owner}
import model.MenageCity.BasicCity.{City, createCity}
import org.junit.*
import org.junit.Assert.*
class BasicCityTest:
  val cityModule: CityModule = BasicCity
  var city: cityModule.City = cityModule.createCity("A", 3)

  @Test
  def GetMethodBasicCityTest(): Unit =
    assertEquals(3,createCity("B",3).getSize)
    assertEquals( "A", city.getName)
    assertEquals( Owner.HUMAN, city.getOwner)

  @Test
  def AIInfectsACityTest(): Unit =
    city = city.conqueredCity()
    assertEquals(city.getOwner, Owner.AI)
    city = city.conqueredCity()
    city = city.conqueredCity()
    assertEquals(city.getOwner, Owner.AI)




