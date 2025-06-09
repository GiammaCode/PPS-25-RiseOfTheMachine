package model.strategy

/**
 * Represents a turn-based action that can be performed by a human player.
 * All human actions have an optional list of targets (cities or regions),
 * and a name for display or logging purposes.
 */
sealed trait HumanAction extends TurnAction:
  /**
   * The list of targets affected by this action.
   * This can be a list of city names or other game entities.
   */
  def targets: List[ActionTarget]

  /**
   * Returns the name of the action as a string.
   */
  def name: String = this match
    case CityDefense(_) => "CityDefense"
    case GlobalDefense(_) => "GlobalDefense"
    case DevelopKillSwitch => "DevelopKillSwitch"


/**
 * Companion object for HumanAction.
 * Provides predefined instances and constructors for user-facing logic.
 */
object HumanAction:
  /**
   * List of all possible human actions (used for UI, validation, etc.).
   */
  val allActions: List[HumanAction] = List(CityDefense(), GlobalDefense(), DevelopKillSwitch)

  /**
   * Creates a CityDefense action with the specified targets.
   *
   * @param targets list of cities to defend
   * @return a CityDefense action instance
   */
  def cityDefense(targets: List[ActionTarget]) : HumanAction = CityDefense(targets)

  /**
   * Creates a GlobalDefense action with the specified targets.
   *
   * @param targets list of cities affected by global defense
   * @return a GlobalDefense action instance
   */
  def globalDefense(targets: List[ActionTarget]): HumanAction = GlobalDefense(targets)

  /**
   * Creates a DevelopKillSwitch action.
   *
   * @return a singleton instance of DevelopKillSwitch
   */
  def developKillSwitch(): HumanAction = DevelopKillSwitch

/**
 * Represents an action where the player defends specific cities.
 *
 * @param targets list of city targets to apply defense to
 */
case class CityDefense(targets: List[ActionTarget] = List.empty) extends HumanAction

/**
 * Represents a global defense action that applies lower-level protection to all cities.
 *
 * @param targets list of city targets affected by global defense
 */
case class GlobalDefense(targets: List[ActionTarget] = List.empty) extends HumanAction

/**
 * Represents an action to invest in developing the kill switch technology.
 * This action has no targets.
 */
case object DevelopKillSwitch extends HumanAction:
  override def targets: List[ActionTarget] = List.empty