package model.map
import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.*
import model.strategy.PlayerAI

object  WorldStateModule:

  opaque type WorldState = (WorldMap,Int)

  private var turn: Int = 0
  val killSwitchPercentage: Int= 0

  def creteWorldState(): WorldState = ???

  private def currentTurn(): Int =
    increaseTurn()
    turn

  private def increaseTurn(): Unit =
    turn = turn + 1
  
  extension(worldState: WorldState)
    def IsGameOver(): Boolean = worldState._1.numberOfCityInfected() > 10//worldMap.AIConquerPercetage > 70
    def getMap: WorldMap = worldState._1
    def attackableCity(f: Int => Int): Set[(String,Int,Int)] = ???
    def getAiCities(): Set[City] = ???
    def getHumanCities(): Set[City] = ???






