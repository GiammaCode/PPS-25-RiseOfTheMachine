package model.strategy.turnAction

import model.strategy.{TurnAction, TurnActionType}

case class EvolveAction() extends TurnAction(EvolveAction, None)

object EvolveAction extends TurnActionType :
  override def execute(targets: Option[List[String]]): String =
    s"Evolving"