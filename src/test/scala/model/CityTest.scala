package model


import model.map.CityModule.CityImpl.{City, createCity}
import model.map.CityModule.Owner
import org.junit.*
import org.junit.Assert.*
class CityTest:
  private val cityBaseDefense = 40

  private val moltiplicator = 5

  private val playerAttack = 20

  private val citySize = 3

  private val capitalSize = 8

  val city: City = createCity("Milano",citySize,isCapital = false)
  val capital: City = createCity("Monza",capitalSize,isCapital = true)


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
    assertEquals(cityBaseDefense + citySize * moltiplicator , city.getDefense)


  @Test
  def sabotageCityTest():Unit =
    assertEquals(cityBaseDefense + (citySize * moltiplicator) - playerAttack,city.sabotateCity(playerAttack).getDefense)

  @Test
  def testMethodOnCapital(): Unit =
    assertNotEquals(capital, createCity("Milano",capitalSize,isCapital = false))
    assertEquals(true , capital.isCapital)
    assertEquals(cityBaseDefense + (capitalSize * moltiplicator), capital.getDefense)
    assertEquals(cityBaseDefense + (capitalSize * moltiplicator) - playerAttack , capital.sabotateCity(playerAttack).getDefense)






