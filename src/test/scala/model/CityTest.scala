package model


import model.map.CityModule.CityImpl.{City, createCity}
import model.map.CityModule.Owner
import org.junit.*
import org.junit.Assert.*
class CityTest:
  var city: City = createCity("Milano",3,isCapital = false)
  @Test
  def GetMethodBasicCityTest(): Unit =
    assertEquals( "Milano", city.getName)
    assertEquals(Owner.HUMAN, city.getOwner)
    assertEquals(false, createCity("Milano",8,isCapital = false).isCapital)

  @Test
  def AIInfectsACityTest(): Unit =
    city = city.infectCity()
    assertEquals(city.getOwner, Owner.AI)

  @Test
  def TestCapitalCreation(): Unit =
    assertNotEquals(createCity("Monza",8,isCapital = true), createCity("Milano",8,isCapital = false))
    assertEquals(true , createCity("Roma",8,isCapital = true).isCapital)
    assertEquals(80, createCity("Roma",8,isCapital = true).getDefense)
    assertEquals(60, createCity("Roma",8,isCapital = true).sabotateCity(20).getDefense)






