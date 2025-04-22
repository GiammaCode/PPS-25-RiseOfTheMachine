package model.strategy

object PlayerActionTypes :
  
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
  import PlayerActionTypes._

  case class SabotageAction(override val targets: List[String] = List.empty)
    extends TurnAction(Sabotage, targets)

  case class InfectAction(override val targets: List[String] = List.empty)
    extends TurnAction(Infect, targets)

  case class EvolveAction() extends TurnAction(Evolve)
