// File: model/strategy/AiAction.scala
package model.strategy

/** Represents actions specifically for AI players */
sealed trait AiAction extends TurnAction :
  def execute: Unit
  def targets: List[Target]

object AiAction {
  def infect(targets: List[Target]): AiAction = Infect(targets)
  def sabotage(targets: List[Target]): AiAction = Sabotage(targets)
  def evolve(): AiAction = Evolve
}
case class Infect(targets: List[Target]) extends AiAction {
  override def execute: Unit = {
    if (targets.isEmpty) println("AI Infect failed: No targets specified")
    else println(s"AI Infect succeeded: Infected ${targets.mkString(", ")}")
  }
}

case class Sabotage(targets: List[Target]) extends AiAction {
  override def execute: Unit = {
    if (targets.isEmpty) println("AI Sabotage failed: No targets specified")
    else println(s"AI Sabotage succeeded: Sabotaged ${targets.mkString(", ")}")
  }
}

case object Evolve extends AiAction {
  override def targets: List[Target] = List.empty
  override def execute: Unit = println("AI Evolution complete")
}