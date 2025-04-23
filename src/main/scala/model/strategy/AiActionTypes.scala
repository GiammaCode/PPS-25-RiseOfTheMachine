package model.strategy

trait AiAction extends TurnAction

object AiActionTypes :
  
  case object Sabotage extends TurnActionType :
    override def execute(targets: List[String]): String =
      s"SabotageAction on ${targets}"
  
  case object Infect extends TurnActionType :
    override def execute(targets: List[String]): String =
      s"InfectAction on ${targets}"

  case object Evolve extends TurnActionType :
    override def execute(targets: List[String]): String =
      s"Evolving"

package object playerActions :
  import AiActionTypes._

  case class SabotageAction(override val targets: List[String] = List.empty)
    extends TurnAction(Sabotage, targets) with AiAction

  case class InfectAction(override val targets: List[String] = List.empty)
    extends TurnAction(Infect, targets) with AiAction

  case class EvolveAction() extends TurnAction(Evolve) with AiAction
