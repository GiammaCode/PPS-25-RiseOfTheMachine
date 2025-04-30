package model.strategy

import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.strategy.humanActions.*

/**
 * Companion object for [[PlayerHuman]].
 * Provides a factory method to create a default instance of a human-controlled player.
 */
object PlayerHuman:
  /**
   * Creates a default PlayerHuman instance.
   *
   * @return a PlayerHuman with default state.
   */
  def default: PlayerHuman = PlayerHuman()

/**
 * Represents a human-controlled player in the strategy game.
 *
 * This player can perform actions such as defending cities or developing a kill switch.
 * The class is immutable: all state modifications return a new instance.
 *
 * @constructor private to enforce creation through the companion object's `apply` method.
 *
 * @param executedActions List of actions executed by the player.
 * @param conqueredCities Set of cities initially under human control.
 * @param killSwitch Progress of the kill switch development.
 * @param defendedCities Set of cities that have been defended.
 */
case class PlayerHuman private(
                        executedActions: List[HumanAction] = List.empty,
                        conqueredCities: Set[String] = Set("A", "B", "c"),
                        killSwitch: Int = 0,
                        defendedCities: Set[String] = Set.empty
                      ) extends PlayerEntity:

  /**
   * Defines the type of actions this player can perform.
   */
  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  /**
   * Executes a given human action and returns the updated player state.
   *
   * @param action the action to execute
   * @return a new PlayerHuman with the action applied
   */
  override def executeAction(action: ValidAction): ExecuteActionResult[Self] =
    doExecuteAction(action)

  /**
   * Returns a string representation of the player's current state.
   *
   * @return human-readable summary
   */
  override def toString : String =
  s"""|--- Human Status ---
      |Conquered Cities     : ${if (conqueredCities.isEmpty) "None" else conqueredCities.mkString(", ")}
      |Defended Cities     : ${if (defendedCities.isEmpty) "None" else defendedCities.mkString(", ")}
      |Executed Actions     :
      |  ${if (executedActions.isEmpty) "None" else executedActions.map(_.execute).mkString("\n  ")}
      |------------------------
     """.stripMargin

  /**
   * Internal handler for action execution.
   */
  private def doExecuteAction(action: HumanAction): ExecuteActionResult[Self] = ??? 
//  action match
//    case action: CityDefenseAction => singleCityDefense(action.targets)
//    case action: GlobalDefenseAction => globalDefense(action.targets)
//    case action: DevelopKillSwitchAction => developKillSwitchAction

  /**
   * Executes a city defense on a single city.
   */
  private def singleCityDefense(city: List[String]): PlayerHuman =
    copy(defendedCities = defendedCities ++ city)
      .addAction(CityDefenseAction(city))

  /**
   * Executes a city defense on a single city.
   */
  private def globalDefense(cities: List[String]) =
    copy(defendedCities = defendedCities ++ cities)
      .addAction(GlobalDefenseAction(cities))

  /**
   * Executes the kill switch development action.
   */
  private def developKillSwitchAction = addAction(DevelopKillSwitchAction())

  /**
   * Adds an executed action to the action history.
   */
  private def addAction(action: HumanAction): PlayerHuman =
    copy(executedActions = action :: executedActions)


