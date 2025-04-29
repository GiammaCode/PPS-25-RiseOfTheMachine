package model.strategy

/** Defines the behavior of a specific type of turn action. */
trait TurnActionType :
  def execute(targets :  List[String]) : String


/** Abstract class for actions that can be executed during a game turn.
 *
 * @param actionType The type of action being performed.
 * @param targets    The targets affected by this action.
 */
abstract class TurnAction(val actionType: TurnActionType, val targets : List[String] = List.empty) :
  /** Executes the action using its action type. */
  def execute: String = actionType.execute(targets)