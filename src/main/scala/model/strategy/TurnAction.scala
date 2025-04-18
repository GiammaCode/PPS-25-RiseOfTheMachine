package model.strategy

abstract class TurnAction(actionType: TurnActionType, targets : Option[List[String]]) :
  def execute: String = s"$actionType on $targets"

