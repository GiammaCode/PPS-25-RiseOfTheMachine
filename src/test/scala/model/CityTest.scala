package model


import model.map.CityModule.CityImpl.{City, createCity}
import model.map.CityModule.Owner
import org.junit.*
import org.junit.Assert.*
class CityTest:
  val city: City = createCity("Milano",3,isCapital = false)
  val capital: City = createCity("Monza",8,isCapital = true)

  @Test
  def getMethodBasicCityTest(): Unit =
    assertEquals( "Milano", city.getName)
    assertEquals(Owner.HUMAN, city.getOwner)
    assertEquals(false, city.isCapital)

  @Test
  def aIInfectsACityTest(): Unit =
    assertEquals(city.infectCity().getOwner, Owner.AI)

  @Test
  def testDefenceOfCity(): Unit =
    assertEquals(55, city.getDefense)

  @Test
  def sabotageCityTest():Unit =
    assertEquals(35,city.sabotateCity(20).getDefense)

  @Test
  def testMethodOnCapital(): Unit =
    assertNotEquals(capital, createCity("Milano",8,isCapital = false))
    assertEquals(true , capital.isCapital)
    assertEquals(80, capital.getDefense)
    assertEquals(60, capital.sabotateCity(20).getDefense)






