package model.strategy

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.WorldMap
import model.map.WorldState.WorldState
import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.util.GameSettings.{Difficulty, GameSettings, HumanStats}

/**
 * Represents the human-controlled player in the strategy model.
 * Extends the common PlayerEntity interface.
 */
trait PlayerHuman extends PlayerEntity:
  /** Current progress of the kill switch development. */
  def killSwitch: Int

  /** Get of cities currently defended by the player. */
  def defendedCities: Set[String]

  /** Get of cities conquered by the player. */
  def conqueredCities: Set[String]

  /** Returns the list of valid actions the player can take. */
  def getPossibleAction: List[HumanAction]

  /** Returns the list of actions already executed. */
  def executedActions: List[HumanAction]

  /** Decides an action using the SmartHumanStrategy based on world state. */
  def decideActionByStrategy(worldState: WorldState): HumanAction

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  /** Executes a given action, modifying the player state. */
  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self]

  override def toString: String

/**
 * Companion object for PlayerHuman.
 * Provides factory methods from settings or stats.
 */
object PlayerHuman:

  @deprecated
  def fromStats(using stats: HumanStats): PlayerHuman =
    PlayerHumanImpl(killSwitch = stats.killSwitch)

  /**
   * Creates a PlayerHuman instance from game settings.
   */
  def fromSettings(using settings: GameSettings): PlayerHuman =
    PlayerHumanImpl(killSwitch = settings.human.killSwitch, actionsWeight = settings.human.actionsWeight)


/**
 * Concrete implementation of the PlayerHuman trait.
 *
 * @param killSwitch      current progress of the kill switch
 * @param defendedCities  cities currently defended
 * @param conqueredCities cities conquered by the player
 * @param executedActions list of executed actions
 */
private case class PlayerHumanImpl(
                                    killSwitch: Int = 0,
                                    actionsWeight: ActionProbabilities = ActionProbabilities(33, 33, 34),
                                    defendedCities: Set[String] = Set.empty,
                                    conqueredCities: Set[String] = Set.empty,
                                    executedActions: List[HumanAction] = List.empty
                                  ) extends PlayerHuman:

  private val CityDefenseBoost = 20
  private val GlobalDefenseBoost = 2
  private val KillSwitchIncrement = 10

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  override def getPossibleAction: List[HumanAction] = HumanAction.allActions

  /**
   * Applies the given action to the player and the world map, producing a result.
   */
  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self] =
    action match
    case CityDefense(targets) =>
      val updated = withDefendedCities(targets.toSet).addAction(action)
      val defendedCities: List[City] = targets.flatMap(cityName =>
        worldMap.getCityByName(cityName).map(_.defenseCity(CityDefenseBoost))
      )
      result(updated, Some(defendedCities), s"CityDefense on: ${targets.mkString(", ")}")

    case GlobalDefense(targets) =>
      val updated = withDefendedCities(defendedCities).addAction(action)
      val updatedCities = targets.flatMap(worldMap.getCityByName).map(_.defenseCity(GlobalDefenseBoost))
      result(updated, Some(updatedCities), "GlobalDefense executed")

    case DevelopKillSwitch =>
      val updated = copy(killSwitch = killSwitch + KillSwitchIncrement).addAction(action)
      result(updated, None, "KillSwitch progress increased")

  override def toString: String =
    def formatSet(label: String, set: Set[String]): String =
      s"$label : ${if set.isEmpty then "None" else set.mkString(", ")}"

    def formatList(label: String, list: List[HumanAction]): String =
      s"$label :\n  ${if list.isEmpty then "None" else list.reverse.mkString("\n  ")}"

    s"""|--- PlayerHuman Status ---
        |KillSwitch Progress : $killSwitch
        |${formatSet("Defended Cities", defendedCities)}
        |${formatSet("Conquered Cities", conqueredCities)}
        |${formatList("Executed Actions", executedActions)}
        |------------------------""".stripMargin

  /**
   * Decides an action based on the current difficulty and game state using SmartHumanStrategy.
   */
  def decideActionByStrategy(worldState: WorldState): HumanAction =
    SmartHumanStrategy.decideAction(worldState)(using actionsWeight)

  private def result(player: PlayerHumanImpl, city: Option[List[City]], message: String): ExecuteActionResult[Self] =
    ExecuteActionResult(player, city, List(message))

  private def withDefendedCities(cities: Set[String]): PlayerHumanImpl =
    copy(defendedCities = defendedCities ++ cities)

  private def addAction(action: HumanAction): PlayerHumanImpl =
    copy(executedActions = action :: executedActions)







