package model.strategy.turnAction

import model.strategy.{TurnAction, TurnActionType}

// Concrete case class representing the Infect action
case class InfectAction(targets: Option[List[String]] = None)
  extends TurnAction(InfectAction, targets) // pass the companion object as the type
    with TurnActionType

// Companion object of InfectAction used as the TurnActionType
object InfectAction extends TurnActionType {
  override def toString: String = "InfectAction"
}


