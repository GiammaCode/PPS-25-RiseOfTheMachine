package model.strategy

import model.map.CityModule.CityImpl.City

/** Represents a target for an action */
type ActionTarget = String // In un sistema più robusto, questo potrebbe essere un tipo più specifico

/** A functional representation of actions that can be executed during a game turn. */
trait TurnAction :
  /** The targets affected by this action */
  def targets: List[ActionTarget]

object TurnAction {}
  /** Creates a new TurnAction with the given execution logic and targets */
  def apply(
             actionTargets: List[ActionTarget] = List.empty
           ): TurnAction = new TurnAction {
    override def targets: List[ActionTarget] = actionTargets
  }