package model.map

import model.map.WorldMapModule.*
import model.strategy.AiAbility.AiAbility
import model.strategy.{AiAction, Evolve, Infect, PlayerAI, PlayerEntity, PlayerHuman, Sabotage}
import model.map.CityModule.CityImpl.City
import model.map.WorldState.playerAI
import model.util.GameSettings.{Difficulty, GameSettings}

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
               playerHuman: PlayerHuman, difficulty: Difficulty, turn: Int)

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
  def createWorldState(worldMap: WorldMap, playerAI: PlayerAI, playerHuman: PlayerHuman, turn: Int)(using settings: GameSettings): WorldState =
    State(worldMap, playerAI, playerHuman, settings.difficulty, turn)

  /**
   * Extension methods available on WorldState instances.
   */
  extension (ws: WorldState)

    /**
     * The list of available game options.
     *
     * @return list of option strings
     */
    def AiOptions: List[String] = playerAI.getPossibleAction.map(_.name) //TODO: fix and exit
    def HumanOptions: List[String] = playerHuman.getPossibleAction.map(_.name)

    /**
     * The current turn number.
     *
     * @return turn index (starting from 0)
     */
    def turn: Int = ws match
      case State(_, _, _, _, t) => t

    def difficulty: Difficulty = ws match
      case State(_, _, _, difficulty, _) => difficulty

    /**
     * Returns the current world map.
     *
     * @return the WorldMap object
     */
    def worldMap: WorldMap = ws match
      case State(map, _, _, _, _) => map

    /**
     * Returns the AI player data.
     *
     * @return PlayerAI instance
     */
    def playerAI: PlayerAI = ws match
      case State(_, ai, _, _, _) => ai

    /**
     * Returns the human player data.
     *
     * @return PlayerHuman instance
     */
    def playerHuman: PlayerHuman = ws match
      case State(_, _, human, _, _) => human

    /**
     * Checks whether the game is over.
     * The game ends if more than 10 cities are infected.
     *
     * @return true if game over, false otherwise and who wins the game
     */
    def isGameOver: (Boolean, Option[PlayerEntity]) = ws match
      case State(map, ai, human, _, _) =>
        val totalCities = map.numberOfCity()
        val conqueredPercentage = (map.numberOfCityInfected().toDouble / totalCities) * 100
        val killSwitchProgress = human.killSwitch
        val capitalConquered = map.capitalConqueredCounter

        if conqueredPercentage >= 50 || capitalConquered == 3  then (true, Some(ai))
        else if killSwitchProgress >= 90 then (true, Some(human))
        else (false, None)


    /**
     * Returns a set of attackable cities with infection and sabotage success rates.
     *
     * @return set of tuples (city name, infection %, sabotage %)
     */
    def attackableCities: Set[(String, Int, Int)] =
      worldMap.getAdjacentCities
        .map(c => (
          c.getName,
          calculatePercentageOfSuccess(c.getDefense, playerAI.infectionPower, ws.difficulty),
          calculatePercentageOfSuccess(c.getDefense, playerAI.sabotagePower, ws.difficulty)))

    /**
     * Returns the set of cities conquered by the AI.
     *
     * @return set of city names
     */
    def AIConqueredCities: Set[String] = ws match
      case State(_, ai, _, _, _) => ai.conqueredCities

    /**
     * Returns the set of cities conquered by the human player.
     *
     * @return set of city names
     */
    def humanConqueredCities: Set[String] = ws match
      case State(_, _, human, _, _) => human.conqueredCities

    /**
     * Returns the set of AI abilities currently unlocked.
     *
     * @return set of AiAbility
     */
    def AIUnlockedAbilities: Set[AiAbility] = ws match
      case State(_, ai, _, _, _) => ai.unlockedAbilities

    /**
     * Returns the infection state as a tuple:
     * (number of infected cities, hardcoded max infection threshold).
     *
     * @return tuple (infected, max)
     */
    def infectionState: (Int, Int) = ws match
      case State(map, _, _, _, _) => (map.numberOfCityInfected(), map.numberOfCity())

    /**
     * Creates a new WorldState with an updated AI player.
     *
     * @param newAI the updated PlayerAI instance
     * @return a new WorldState with the new AI
     */
    def updatePlayer(newAI: PlayerAI): WorldState = ws match
      case State(map, _, human, difficulty, turn) => State(map, newAI, human, difficulty, turn)

    /**
     * Creates a new WorldState with an updated human player.
     *
     * @param newHuman the updated PlayerHuman instance
     * @return a new WorldState with the new Human
     */
    def updateHuman(newHuman: PlayerHuman): WorldState = ws match
      case State(map, ai, _, difficulty, turn) => State(map, ai, newHuman, difficulty, turn)

    /**
     * Creates a new WorldState with an updated city in the map.
     * The new city replaces the existing one with the same name.
     *
     * @param newCities the updated City instance
     * @return a new WorldState with the updated map
     */
    def updateMap(newCities: Option[List[City]]): WorldState = ws match
      case State(map, ai, human, difficulty, turn) =>
        val updatedMap = newCities match
          case Some(cityList) => cityList.foldLeft(map)((m, city) => m.changeACityOfTheMap(city))
          case None           => map
        State(updatedMap, ai, human, difficulty, turn)
    /**
     * Creates a new WorldState with an updated play turn.
     *
     * @return a new WorldState with the updated turn
     */
    def updateTurn: WorldState = ws match
      case State(map, ai, human, difficulty, turn) =>
        val newTurn = turn + 1
        State (map, ai, human, difficulty, newTurn)


    /**
     * Returns the success percentage for a given city and action type.
     *
     * @param cityName   the name of the target city
     * @param action the action type: "infect" or "sabotage" or "evolve"
     * @return the success percentage wrapped in Option, or None if invalid
     */
    def probabilityByCityandAction(cityName: String, action: AiAction): Int =
      attackableCities.find(_._1 == cityName).map{
        case(_,infect, sabotage) => action match
          case Infect(_) => infect
          case Sabotage(_) => sabotage
          case Evolve => 100
      }.getOrElse(100)

    /**
     * Private method to calculate a percentage of success
     * of an attack.
     *
     * @param cityDefense       the value of city defense
     * @param playerAttackValue the value of player attack
     * @return a value to success
     */
    private def calculatePercentageOfSuccess(cityDefense: Int, playerAttackValue: Int, difficulty: Difficulty): Int =
      val rawValue = difficulty match
      case Difficulty.Easy => 110 - cityDefense + playerAttackValue
      case Difficulty.Normal => 100 - cityDefense + playerAttackValue
      case Difficulty.Hard => 90 - cityDefense + playerAttackValue

      Math.max(5, Math.min(95, rawValue))




