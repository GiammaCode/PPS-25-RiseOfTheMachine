package model.strategy.turnAction

import model.strategy.{TurnAction, TurnActionType}

case class GlobalDefenseAction(targets: Option[List[String]] = None)
  extends TurnAction(GlobalDefenseAction, targets)

object GlobalDefenseAction extends TurnActionType :
  override def execute(targets: Option[List[String]]): String =
    s"GlobalDefenseAction on ${targets}"
