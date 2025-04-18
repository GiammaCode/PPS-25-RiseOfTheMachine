package model.strategy.turnAction

import model.strategy.{TurnAction, TurnActionType}

case class SabotageAction(targets: Option[List[String]] = None)
    extends TurnAction(SabotageAction, targets)

object SabotageAction extends TurnActionType :
  override def execute(targets: Option[List[String]]): String =
    s"SabotageAction on ${targets}"