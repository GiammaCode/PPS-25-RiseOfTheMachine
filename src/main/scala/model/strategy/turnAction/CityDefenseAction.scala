package model.strategy.turnAction

import model.strategy.{TurnAction, TurnActionType}

case class CityDefenseAction(targets: Option[List[String]] = None)
  extends TurnAction(CityDefenseAction, targets)

object CityDefenseAction extends TurnActionType :
  override def execute(targets: Option[List[String]]): String =
    s"CityDefenseAction on ${targets}"
