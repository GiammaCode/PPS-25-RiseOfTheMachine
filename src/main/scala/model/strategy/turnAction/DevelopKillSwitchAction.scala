package model.strategy.turnAction

import model.strategy.{TurnAction, TurnActionType}

case class DevelopKillSwitchAction(targets: Option[List[String]] = None)
  extends TurnAction(DevelopKillSwitchAction, targets)

object DevelopKillSwitchAction extends TurnActionType :
  override def execute(targets: Option[List[String]]): String =
    s"DevelopKillSwitchAction is done"

