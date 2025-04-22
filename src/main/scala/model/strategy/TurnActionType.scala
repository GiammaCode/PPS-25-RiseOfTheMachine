package model.strategy

trait TurnActionType :
  def execute(targets :  List[String]) : String

abstract class TurnAction(val actionType: TurnActionType, val targets : List[String] = List.empty) :
  def execute: String = actionType.execute(targets)

