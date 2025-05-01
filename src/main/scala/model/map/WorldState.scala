package model.map

import model.map.WorldMapModule.*
import model.strategy.AiAbility.AiAbility
import model.strategy.{PlayerAI, PlayerHuman}
import model.util.Util.*
import model.map.CityModule.CityImpl.City

object WorldState:

  // Implementazione interna: completamente nascosta
  private enum WorldStateImpl:
    case State(worldMap: WorldMap, playerAI: PlayerAI,
               playerHuman: PlayerHuman, turn: Int = 0)

  import WorldStateImpl.*

  // Opaque type esposto esternamente
  opaque type WorldState = WorldStateImpl

  // Costruttore
  def createWorldState(worldMap: WorldMap, playerAI: PlayerAI, playerHuman: PlayerHuman): WorldState =
    State(worldMap, playerAI, playerHuman)

  // Metodi estesi (come map nella slide)
  extension (ws: WorldState)

    def isGameOver: Boolean = ws match
      case State(map, _, _, _) => map.numberOfCityInfected() > 10

    def attackableCities: Set[(String, Int, Int)] = ws match
      case State(map, _, _, _) =>
        map.cities.map { (city, _) =>
          val name = city.getName
          (name, calculatePercentageOfSuccess, calculatePercentageOfSuccess)
        }

    def AIConqueredCities: Set[String] = ws match
      case State(_, ai, _, _) => ai.conqueredCities

    def humanConqueredCities: Set[String] = ws match
      case State(_, _, human, _) => human.conqueredCities

    def turn: Int = ws match
      case State(_, _, _, t) => t

    def worldMap: WorldMap = ws match
      case State(map, _, _, _) => map

    def playerAI: PlayerAI = ws match
      case State(_, ai, _, _) => ai

    def playerHuman: PlayerHuman = ws match
      case State(_, _, human, _) => human

    def AIUnlockedAbilities: Set[AiAbility] = ws match
      case State(_, ai, _, _) => ai.unlockedAbilities

    def infectionState: (Int, Int) = ws match
      case State(map, _, _, _) => (map.numberOfCityInfected(), 15)

    def options: List[String] = List("Opt1", "Opt2", "Exit")

    // Esempio update immutabile (puoi definire altri update simili)
    def updatePlayer(newAI: PlayerAI): WorldState = ws match
      case State(map, _, human, t) => State(map, newAI, human, t)

    //metodo che presa una city ritrna un WorldState,
    //player e human rimangono uguale e io chiamo il metodo changeACityOfTheMap
    def updateMap(newCity: City): WorldState = ws match
      case State(map, ai, human, t) =>
        val updatedMap = map.changeACityOfTheMap(newCity)
        State(updatedMap, ai, human, t)

