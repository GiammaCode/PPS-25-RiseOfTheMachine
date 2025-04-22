
import model.map.CityModule.CityImpl.{City, createCapital, createCity}
import model.map.CityModule.Owner
import org.junit.*
import org.junit.Assert.*
class CityTest:
  var city: City = createCity("A",3)
  @Test
  def GetMethodBasicCityTest(): Unit =
    assertEquals( "A", city.getName)
    assertEquals(Owner.HUMAN, city.getOwner)

  @Test
  def AIInfectsACityTest(): Unit =
    city = city.conqueredCity()
    assertEquals(city.getOwner, Owner.AI)
    city = city.conqueredCity()
    city = city.conqueredCity()
    assertEquals(city.getOwner, Owner.AI)

  @Test
  def TestCapitalCreation(): Unit = createCapital("Monza",8)




