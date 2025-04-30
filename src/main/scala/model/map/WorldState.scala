package model.map

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.*
import model.strategy.AiAbility.AiAbility
import model.strategy.PlayerAI.PlayerAI
import model.strategy.{PlayerAI, PlayerEntity, PlayerHuman}
import model.strategy.PlayerHuman.PlayerHuman
import model.util.Util.*

object WorldState:
  opaque type WorldState = WorldStateImpl
  def createWorldState(worldMap: WorldMap, playerAI: PlayerAI,
                       playerHuman: PlayerHuman): WorldState = WorldStateImpl(worldMap, playerAI, playerHuman)
  extension (worldState: WorldState)
    def IsGameOver: Boolean = worldState.isGameOver
    def attackableCities: Set[(String, Int, Int)] = worldState.getAttackableCities
    def AIConqueredCities: Set[String] = worldState.getAIconqueredCities
    def humanConqueredCities: Set[String] = worldState.getHumaconqueredCities
    def turn: Int = worldState.getTurn
    def worldMap: WorldMap = worldState.getWorldMap
    def AIUnlockedAbilities: Set[AiAbility] = worldState.getAIUnlockedAbilities
    def infectionState: (Int, Int) = worldState.getInfectionState
    def options: List[String] = worldState.getOptions



private case class WorldStateImpl(worldMap: WorldMap, playerAI: PlayerAI,
                                  playerHuman: PlayerHuman, turn: Int = 0):

  val killSwitchPercentage: Int = 0
  def isGameOver: Boolean =
    worldMap.numberOfCityInfected() > 10

  def getAttackableCities: Set[(String, Int, Int)]  = worldMap
    .cities.map { (city, _) => val name = city.getName
      (name, calculatePercentageOfSuccess, calculatePercentageOfSuccess)
    }
  def getAIconqueredCities: Set[String] = playerAI.conqueredCities

  def getHumaconqueredCities: Set[String] = playerHuman.conqueredCities

  def getTurn: Int = turn

  def getWorldMap: WorldMap = worldMap

  def getAIUnlockedAbilities: Set[AiAbility] = playerAI.unlockedAbilities

  //need to change hardcoded
  def getInfectionState: (Int, Int) = (worldMap.numberOfCityInfected(), 15)

  def getOptions: List[String] = List("Opt1", "Opt2", "Exit")














