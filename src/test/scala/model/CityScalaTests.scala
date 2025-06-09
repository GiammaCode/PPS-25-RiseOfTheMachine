package model

import model.map.CityModule
import model.map.CityModule.CityImpl.createCity
import model.map.CityModule.*
import model.map.CityModule.Owner.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CityScalaTests extends AnyFunSuite with Matchers:

  private val baseCityDefence = 40
  private val moltiplicator = 5
  private val citySize = 3

  test("createCity should initialize a city with correct values") {
      val city = createCity("Rome", citySize, isCapital = true)

      city.getName shouldBe "Rome"
      city.getSize shouldBe citySize
      city.getOwner shouldBe HUMAN
      city.getDefense shouldBe baseCityDefence + (citySize * moltiplicator) // 55
    }

    test("infectCity should change owner to AI") {
      val city = createCity("Milan", baseCityDefence, isCapital = false)
      val infected = city.infectCity()

      infected.getOwner shouldBe AI
      infected.getName shouldBe "Milan" // immutable name
    }

    test("sabotateCity should reduce defense by given amount") {
      val city = createCity("Venice", citySize, isCapital = false)
      val playerAttack = 15
      val sabotaged = city.sabotateCity(playerAttack)

      val expectedDefense = (baseCityDefence - playerAttack) + (citySize * moltiplicator) // 25 + 20 = 45
      sabotaged.getDefense shouldBe expectedDefense
    }

    test("defenseCity should increase base defense by 20") {
      val city = createCity("Naples", citySize, isCapital = false)
      val defenseImprove = 20
      val defended = city.defenseCity(defenseImprove)

      val expectedDefense = (baseCityDefence + defenseImprove) + (citySize * moltiplicator) // 60 + 5 = 65
      defended.getDefense shouldBe expectedDefense
    }

    test("city should remain immutable after operations") {
      val city = createCity("Turin", citySize, isCapital = false)
      val infected = city.infectCity()
      val sabotagePower = 10
      val sabotaged = city.sabotateCity(sabotagePower)

      city.getOwner shouldBe HUMAN
      city.getDefense shouldBe baseCityDefence + (citySize * moltiplicator) // 50

      infected.getOwner shouldBe AI
      sabotaged.getDefense shouldBe baseCityDefence + (citySize * moltiplicator) - sabotagePower // 40
    }
