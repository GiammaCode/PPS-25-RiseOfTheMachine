package model

import model.map.CityModule
import model.map.CityModule.CityImpl.createCity
import model.map.CityModule.*
import model.map.CityModule.Owner.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CityScalaTests extends AnyFunSuite with Matchers:

    test("createCity should initialize a city with correct values") {
      val city = createCity("Rome", 3, isCapital = true)

      city.getName shouldBe "Rome"
      city.getSize shouldBe 3
      city.getOwner shouldBe HUMAN
      city.getDefense shouldBe 40 + (3 * 5) // 55
    }

    test("infectCity should change owner to AI") {
      val city = createCity("Milan", 2, isCapital = false)
      val infected = city.infectCity()

      infected.getOwner shouldBe AI
      infected.getName shouldBe "Milan" // immutable name
    }

    test("sabotateCity should reduce defense by given amount") {
      val city = createCity("Venice", 4, isCapital = false)
      val sabotaged = city.sabotateCity(15)

      val expectedDefense = (40 - 15) + (4 * 5) // 25 + 20 = 45
      sabotaged.getDefense shouldBe expectedDefense
    }

    test("defenseCity should increase base defense by 20") {
      val city = createCity("Naples", 1, isCapital = false)
      val defended = city.defenseCity(20)

      val expectedDefense = (40 + 20) + (1 * 5) // 60 + 5 = 65
      defended.getDefense shouldBe expectedDefense
    }

    test("city should remain immutable after operations") {
      val city = createCity("Turin", 2, isCapital = false)
      val infected = city.infectCity()
      val sabotaged = city.sabotateCity(10)

      city.getOwner shouldBe HUMAN
      city.getDefense shouldBe 40 + (2 * 5) // 50

      infected.getOwner shouldBe AI
      sabotaged.getDefense shouldBe 30 + (2 * 5) // 40
    }
