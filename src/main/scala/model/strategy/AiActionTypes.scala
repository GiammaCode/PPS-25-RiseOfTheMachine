package model.strategy

/** Represents an action that can be performed by an AI player.
 * Extends the generic `TurnAction`.
 */
trait AiAction extends TurnAction

/** Defines the possible action types for an AI player. */
object AiActionTypes :

  /** Sabotage action type. Used to lower the defence of a city. */
  case object Sabotage extends TurnActionType :
    override def execute(targets: List[String]): String =
      s"SabotageAction on ${targets}"

  /** Infect action type. Used to conquer a city. */
  case object Infect extends TurnActionType :
    override def execute(targets: List[String]): String =
      s"InfectAction on ${targets}"

  /** Evolve action type. Used to unlock new abilities. */
  case object Evolve extends TurnActionType :
    override def execute(targets: List[String]): String =
      s"Evolving"

package object playerActions :
  import AiActionTypes._

  /** A sabotage action performed by the AI. */
  case class SabotageAction(override val targets: List[String] = List.empty)
    extends TurnAction(Sabotage, targets) with AiAction

  /** An infect action performed by the AI. */
  case class InfectAction(override val targets: List[String] = List.empty)
    extends TurnAction(Infect, targets) with AiAction

  /** An evolve action performed by the AI. */
  case class EvolveAction() extends TurnAction(Evolve) with AiAction
