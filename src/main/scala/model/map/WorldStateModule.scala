package model.map

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.*
import model.strategy.PlayerAI.PlayerAI
import model.strategy.PlayerHuman.PlayerHuman
import model.util.Util.*

object WorldStateModule:

  opaque type WorldState = (WorldMap, PlayerAI, PlayerHuman, Int)

  private var turn: Int = 0
  val killSwitchPercentage: Int = 0

  def createWorldState(): WorldState = ???

  private def currentTurn(): Int =
    increaseTurn()
    turn

  private def increaseTurn(): Unit =
    turn = turn + 1

  extension (worldState: WorldState)
    def IsGameOver(): Boolean = worldState._1.numberOfCityInfected() > 10 //worldMap.AIConquerPercetage > 70

    def getMap: WorldMap = worldState._1

    def attackableCity: Set[(String, Int, Int)] = getMap.cities.map { (city, _) =>
      val name = city.getName
      (name, calculatePercentageOfSuccess, calculatePercentageOfSuccess)
    }
    //def getAiCities(player :PlayerAI): Set[City] = player.conqueredCities
    //def getHumanCities(player: PlayerHuman): Set[City] = player.conqueredCities






