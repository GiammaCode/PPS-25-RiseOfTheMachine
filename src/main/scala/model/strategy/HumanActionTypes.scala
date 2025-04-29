package model.strategy
trait HumanAction extends TurnAction

object HumanActionTypes:

  case object CityDefense extends TurnActionType:
    override def execute(targets: List[String]): String =
      s"CityDefenseAction on ${targets}"

  case object GlobalDefense extends TurnActionType:
    override def execute(targets: List[String]): String =
      s"GlobalDefenseAction on ${targets}"

  case object DevelopKillSwitch extends TurnActionType:
    override def execute(targets: List[String]): String =
      s"DevelopKillSwitchAction is done"

package object humanActions:
  import HumanActionTypes._

  case class CityDefenseAction(override val targets: List[String] = List.empty)
    extends TurnAction(CityDefense, targets) with HumanAction

  case class GlobalDefenseAction(override val targets: List[String] = List.empty)
    extends TurnAction(GlobalDefense, targets)  with HumanAction

  case class DevelopKillSwitchAction(override val targets: List[String] = List.empty)
    extends TurnAction(DevelopKillSwitch, targets)  with HumanAction


