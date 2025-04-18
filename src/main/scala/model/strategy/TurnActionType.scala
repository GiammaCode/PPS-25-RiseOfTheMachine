package model.strategy

trait TurnActionType :
  def execute(targets :  Option[List[String]]) : String