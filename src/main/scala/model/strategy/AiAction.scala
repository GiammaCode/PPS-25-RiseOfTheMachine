package model.strategy

/** Represents actions specifically for AI players */
sealed trait AiAction extends TurnAction :
  /**
   * The list of targets (e.g., cities) that the action is intended to affect.
   */
  def targets: List[ActionTarget]

  /**
   * The name of the action
   *
   * @return A string representing the action type.
   */
  def name: String = this match
    case Infect(_) => "Infect"
    case Sabotage(_) => "Sabotage"
    case Evolve => "Evolve"

/**
 * Companion object for [[AiAction]].
 * Provides utility methods for working with AI actions.
 */
object AiAction :
  /**
   * A complete list of all possible AI actions.
   */
  val allActions: List[AiAction] = List(Infect(), Sabotage(), Evolve)

  /**
   * Factory method to create an Infect action with the specified targets.
   *
   * @param targets A list of targets (e.g., city names).
   * @return An instance of [[Infect]].
   */
  def infect(targets: List[ActionTarget]): AiAction = Infect(targets)

  /**
   * Factory method to create a Sabotage action with the specified targets.
   *
   * @param targets A list of targets (e.g., city names).
   * @return An instance of [[Sabotage]].
   */
  def sabotage(targets: List[ActionTarget]): AiAction = Sabotage(targets)

  /**
   * Factory method to create an Evolve action.
   *
   * @return An instance of [[Evolve]].
   */
  def evolve(): AiAction = Evolve

/**
 * Action that allows the AI to attempt infection of one or more target cities.
 *
 * @param targets The list of cities to infect.
 */
case class Infect(targets: List[ActionTarget] = List.empty) extends AiAction

/**
 * Action that allows the AI to sabotage one or more target cities.
 *
 * @param targets The list of cities to sabotage.
 */
case class Sabotage(targets: List[ActionTarget] = List.empty) extends AiAction

/**
 * Action that allows the AI to evolve by unlocking a new ability.
 */
case object Evolve extends AiAction :
  override def targets: List[ActionTarget] = List.empty
