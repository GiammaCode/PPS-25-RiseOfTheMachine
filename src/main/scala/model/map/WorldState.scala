package model.map

import model.map.WorldMapModule.*
import model.strategy.AiAbility.AiAbility
import model.strategy.{AiAction, Infect, PlayerAI, PlayerHuman, Sabotage}
import model.util.Util.*
import model.map.CityModule.CityImpl.City
/**
 * The `WorldState` module represents the full game state at a given turn.
 * It includes information about the world map, both players, and the current turn.
 *
 * The implementation is encapsulated using an opaque type, ensuring immutability and functional purity.
 * All operations on `WorldState` are provided via extension methods.
 */
object WorldState:

  /**
   * Internal representation of the world state.
   * Not visible to external modules.
   */
  private enum WorldStateImpl:
    case State(worldMap: WorldMap, playerAI: PlayerAI,
               playerHuman: PlayerHuman, turn: Int = 0)

  import WorldStateImpl.*

  opaque type WorldState = WorldStateImpl

  /**
   * Creates a new `WorldState` with the given map and players.
   *
   * @param worldMap    the initial world map
   * @param playerAI    the AI player
   * @param playerHuman the human player
   * @return a new WorldState instance
   */
  def createWorldState(worldMap: WorldMap, playerAI: PlayerAI, playerHuman: PlayerHuman): WorldState =
    State(worldMap, playerAI, playerHuman)

  /**
   * Extension methods available on WorldState instances.
   */
  extension (ws: WorldState)

    /**
     * The list of available game options.
     *
     * @return list of option strings
     */
    def options: List[String] = playerAI.getPossibleActionByName //TODO: fix and exit

    /**
     * The current turn number.
     *
     * @return turn index (starting from 0)
     */
    def turn: Int = ws match
      case State(_, _, _, t) => t

    /**
     * Returns the current world map.
     *
     * @return the WorldMap object
     */
    def worldMap: WorldMap = ws match
      case State(map, _, _, _) => map

    /**
     * Returns the AI player data.
     *
     * @return PlayerAI instance
     */
    def playerAI: PlayerAI = ws match
      case State(_, ai, _, _) => ai

    /**
     * Returns the human player data.
     *
     * @return PlayerHuman instance
     */
    def playerHuman: PlayerHuman = ws match
      case State(_, _, human, _) => human

    /**
     * Checks whether the game is over.
     * The game ends if more than 10 cities are infected.
     *
     * @return true if game over, false otherwise
     */
    def isGameOver: Boolean = ws match
      case State(map, _, _, _) => map.numberOfCityInfected() > 10

    /**
     * Returns a set of attackable cities with infection and sabotage success rates.
     *
     * @return set of tuples (city name, infection %, sabotage %)
     */
    def attackableCities: Set[(String, Int, Int)] = ws match
      case State(map, _, _, _) =>
        map.cities.map { (city, _) =>
          val name = city.getName
          val infectChance = calculatePercentageOfSuccess(city.getDefense, playerAI.infectionChance)
          val sabotageChance = calculatePercentageOfSuccess(city.getDefense, playerAI.sabotagePower)
          (name, infectChance, sabotageChance)
        }

    /**
     * Returns the set of cities conquered by the AI.
     *
     * @return set of city names
     */
    def AIConqueredCities: Set[String] = ws match
      case State(_, ai, _, _) => ai.conqueredCities

    /**
     * Returns the set of cities conquered by the human player.
     *
     * @return set of city names
     */
    def humanConqueredCities: Set[String] = ws match
      case State(_, _, human, _) => human.conqueredCities

    /**
     * Returns the set of AI abilities currently unlocked.
     *
     * @return set of AiAbility
     */
    def AIUnlockedAbilities: Set[AiAbility] = ws match
      case State(_, ai, _, _) => ai.unlockedAbilities

    /**
     * Returns the infection state as a tuple:
     * (number of infected cities, hardcoded max infection threshold).
     *
     * @return tuple (infected, max)
     */
    def infectionState: (Int, Int) = ws match
      case State(map, _, _, _) => (map.numberOfCityInfected(), 15)

    /**
     * Creates a new WorldState with an updated AI player.
     *
     * @param newAI the updated PlayerAI instance
     * @return a new WorldState with the new AI
     */
    def updatePlayer(newAI: PlayerAI): WorldState = ws match
      case State(map, _, human, t) => State(map, newAI, human, t)

    /**
     * Creates a new WorldState with an updated human player.
     *
     * @param newHuman the updated PlayerHuman instance
     * @return a new WorldState with the new Human
     */
    def updateHuman(newHuman: PlayerHuman): WorldState = ws match
      case State(map, ai, _, t) => State(map, ai, newHuman, t)

    /**
     * Creates a new WorldState with an updated city in the map.
     * The new city replaces the existing one with the same name.
     *
     * @param newCity the updated City instance
     * @return a new WorldState with the updated map
     */
    def updateMap(newCity: City): WorldState = ws match
      case State(map, ai, human, t) =>
        val updatedMap = map.changeACityOfTheMap(newCity)
        State(updatedMap, ai, human, t)

    /**
     * Private method to calculate a percentage of success
     * of an attack.
     *
     * @param cityDefense the value of city defense
     * @param playerAttackValue the value of player attack
     * @return a value to success
     */
    private def calculatePercentageOfSuccess(cityDefense: Int, playerAttackValue: Int): Int =
      100 - cityDefense + playerAttackValue
