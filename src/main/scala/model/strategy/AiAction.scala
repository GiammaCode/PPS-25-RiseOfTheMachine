// File: model/strategy/AiAction.scala
package model.strategy

/** Represents actions specifically for AI players */
sealed trait AiAction extends TurnAction :
  def targets: List[ActionTarget]
  def name: String = this match
    case Infect(_) => "Infect"
    case Sabotage(_) => "Sabotage"
    case Evolve => "Evolve"

object AiAction :
  val allActions: List[AiAction] = List(Infect(), Sabotage(), Evolve)
  def infect(targets: List[ActionTarget]): AiAction = Infect(targets)
  def sabotage(targets: List[ActionTarget]): AiAction = Sabotage(targets)
  def evolve(): AiAction = Evolve

case class Infect(targets: List[ActionTarget] = List.empty) extends AiAction

case class Sabotage(targets: List[ActionTarget] = List.empty) extends AiAction

case object Evolve extends AiAction :
  override def targets: List[ActionTarget] = List.empty
