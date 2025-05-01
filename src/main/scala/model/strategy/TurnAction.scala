package model.strategy

/** Represents a target for an action */
type ActionTarget = String // In un sistema più robusto, questo potrebbe essere un tipo più specifico

/** A functional representation of actions that can be executed during a game turn. */
trait TurnAction :
  /** The targets affected by this action */
  def targets: List[ActionTarget]

  /** Execute function that performs the logic of this action. Returns Unit as there's no explicit result object. */
  def execute: Unit // Modificato per restituire Unit

object TurnAction {}
  /** Creates a new TurnAction with the given execution logic and targets */
  def apply(
             executeLogic: List[ActionTarget] => Unit, // Modificato per accettare funzione che restituisce Unit
             actionTargets: List[ActionTarget] = List.empty
           ): TurnAction = new TurnAction {
    override def targets: List[ActionTarget] = actionTargets

    override def execute: Unit = executeLogic(targets) // Chiama la funzione che restituisce Unit
  }