package model.strategy

import model.map.WorldState.WorldState
import model.util.GameSettings.Difficulty

import scala.util.Random

/**
 * Represents the relative weights (probabilities) assigned to each possible HumanAction
 * that a strategy can choose from during decision-making.
 *
 * These weights are used to influence the likelihood of each action being selected
 * by weighted random selection logic.
 *
 * The higher the weight, the more likely the action is to be chosen.
 * A weight of zero effectively disables that action.
 *
 * @param cityDefenseWeight     the weight assigned to the CityDefense action
 * @param globalDefenseWeight   the weight assigned to the GlobalDefense action
 * @param killSwitchWeight      the weight assigned to the DevelopKillSwitch action
 */
case class ActionProbabilities(
                                cityDefenseWeight: Int,
                                globalDefenseWeight: Int,
                                killSwitchWeight: Int
                              )


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
  def decideAction(state: WorldState)(using probs: ActionProbabilities): A

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
  override def decideAction(state: WorldState)(using probs: ActionProbabilities): HumanAction =
    def decideWeighted(targetSelector: WorldState => List[String]): HumanAction =
      val targets = targetSelector(state)
      val defend = nonDefended(targets, state).take(1)
      val available = baseHumanActions(defend, targets)
      val actions = available.collect {
        case a: CityDefense => (a, probs.cityDefenseWeight)
        case a: GlobalDefense => (a, probs.globalDefenseWeight)
        case DevelopKillSwitch => (DevelopKillSwitch, probs.killSwitchWeight)
      }
      chooseWeighted(actions)

    state.difficulty match
      case Difficulty.Easy => decideWeighted(possibleTargets)
      case Difficulty.Normal => decideWeighted(possibleTargets)
      case Difficulty.Hard => decideWeighted(topRiskTargets)


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

  /**
   * Selects one action from a list of weighted HumanActions using a weighted random algorithm.
   *
   * Each action in the input list is associated with a weight (priority or likelihood).
   * The selection is made such that the probability of choosing an action is proportional to its weight.
   *
   * @param actions a list of tuples where each tuple contains a HumanAction and its corresponding weight
   * @return one HumanAction randomly selected based on the specified weights
   */
  private def chooseWeighted(actions: List[(HumanAction, Int)]): HumanAction =
    val total = actions.map(_._2).sum
    val rand = Random.nextInt(total)
    actions
      .scanLeft((Option.empty[HumanAction], 0)) {
        case ((_, acc), (action, weight)) => (Some(action), acc + weight)
      }
      .collectFirst { case (Some(action), acc) if rand < acc => action }
      .get

