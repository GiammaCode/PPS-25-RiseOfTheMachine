package model.strategy

/** Represents a target for an action */
type ActionTarget = String

/** A functional representation of actions that can be executed during a game turn. */
trait TurnAction :
  /** The targets affected by this action */
  def targets: List[ActionTarget]

object TurnAction {}
  /**
   * @param actionTargets, list of target to apply the action to
   *  @return  a new TurnAction with the given execution logic and targets
   * */
  def apply(
             actionTargets: List[ActionTarget] = List.empty
           ): TurnAction = new TurnAction {
    override def targets: List[ActionTarget] = actionTargets
  }