package model.strategy

import model.map.WorldState.WorldState
import model.util.GameSettings.Difficulty

import scala.util.Random

/**
 * A generic trait for a player strategy that can decide a valid TurnAction
 * based on the current WorldState.
 *
 * @tparam A the type of TurnAction the strategy produces
 */
trait PlayerStrategy[A <: TurnAction]:
  /**
   * Decides the next action to perform based on the current game state.
   *
   * @param state the current state of the game world
   * @return a valid TurnAction for the player
   */
  def decideAction(state: WorldState): A

/**
 * A smart strategy implementation for the Human player.
 * The decision is based on the difficulty level defined in the game settings.
 *
 * - Easy: defensive/passive
 * - Normal: random among all valid actions
 * - Hard: risk-based and proactive
 */
object SmartHumanStrategy extends PlayerStrategy[HumanAction]:
  /**
   * Chooses the appropriate decision strategy based on the difficulty level.
   *
   * @param state the current world state
   * @return the chosen HumanAction
   */
  override def decideAction(state: WorldState): HumanAction =

    /**
     * Easy strategy: passive defense with occasional global protection.
     */
    def decideEasy: HumanAction =
      val defend = nonDefended(possibleTargets(state), state).take(1)
      val actions = List(
        Option.when(defend.nonEmpty)(CityDefense(defend)),
        Some(GlobalDefense())
      ).flatten
      Random.shuffle(actions).head

    /**
     * Normal strategy: random among available actions including defense,
     * kill switch development, and global defense.
     */
    def decideNormal: HumanAction =
      val defend = nonDefended(possibleTargets(state), state).take(1)
      val actions = baseHumanActions(defend, possibleTargets(state))
      Random.shuffle(actions).head

    /**
     * Hard strategy: prioritize highest-risk cities and act proactively.
     */
    def decideHard: HumanAction =
      val defend = nonDefended(topRiskTargets(state), state).take(1)
      val actions = baseHumanActions(defend, topRiskTargets(state))
      Random.shuffle(actions).head


    state.difficulty match
      case Difficulty.Easy => decideEasy
      case Difficulty.Normal => decideNormal
      case Difficulty.Hard => decideHard


  /**
   * Extracts the list of attackable cities as names, without sorting.
   *
   * @param state the current game state
   * @return list of city names that can be targeted
   */
  private def possibleTargets(state: WorldState): List[String] =
    state.attackableCities.map(_._1).toList

  /**
   * Returns the names of attackable cities sorted by combined infection and sabotage risk (descending).
   *
   * @param state the current game state
   * @return list of city names ordered by total risk
   */
  private def topRiskTargets(state: WorldState): List[String] =
    state.attackableCities.toList
      .sortBy { case (_, infect, sabotage) => -(infect + sabotage) }
      .map(_._1)

  /**
   * Filters out cities that are already defended by the human player.
   *
   * @param cities list of candidate city names
   * @param state  the current game state
   * @return list of city names not yet defended
   */
  private def nonDefended(cities: List[String], state: WorldState): List[String] =
    cities.filterNot(state.playerHuman.defendedCities.contains)

  /**
   * Builds the list of available actions for the human player,
   * including city defense, kill switch development, and global defense.
   *
   * @param defend     list of cities to defend
   * @param allTargets list of all potential city targets
   * @return a list of valid HumanActions
   */
  private def baseHumanActions(defend: List[String], allTargets: List[String]): List[HumanAction] =
    List(
      Option.when(defend.nonEmpty)(CityDefense(defend)),
      Some(DevelopKillSwitch),
      Some(GlobalDefense(allTargets))
    ).flatten

