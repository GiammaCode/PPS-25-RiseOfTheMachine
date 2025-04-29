package model


import model.map.CityModule.CityImpl.{City, createCapital, createCity}
import model.map.CityModule.Owner
import org.junit.*
import org.junit.Assert.*
class CityTest:
  var city: City = createCity("Milano",3)
  @Test
  def GetMethodBasicCityTest(): Unit =
    assertEquals( "Milano", city.getName)
    assertEquals(Owner.HUMAN, city.getOwner)
    assertEquals(false, createCity("Milano",8).isCapital)

  @Test
  def AIInfectsACityTest(): Unit =
    city = city.infectCity()
    assertEquals(city.getOwner, Owner.AI)

  @Test
  def TestCapitalCreation(): Unit =
    assertNotEquals(createCity("Monta",8), createCapital("Monza",8))
    assertEquals(true , createCapital("Roma",8).isCapital)
    assertEquals(80, createCapital("Roma",8).getDefense)
    assertEquals(60, createCapital("Roma",8).sabotateCity().getDefense)






