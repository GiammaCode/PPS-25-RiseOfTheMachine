package model.strategy

/** Represents actions specifically for human players */
sealed trait HumanAction extends TurnAction:
  def targets: List[ActionTarget]
/*
  def name: String = this match
    case CityDefense(_) => "CityDefense"
    case GlobalDefense(_) => "GlobalDefense"
    case DevelopKillSwitch(_) => "DevelopKillSwitch"

*/
/*
  def execute: Unit

object HumanAction:
  def cityDefense(targets: List[ActionTarget]): HumanAction = CityDefense(targets)
  def globalDefense(targets: List[ActionTarget]): HumanAction = GlobalDefense(targets)
  def developKillSwitch(): HumanAction = DevelopKillSwitch

/** Concrete implementation: CityDefense */
case class CityDefense(targets: List[ActionTarget] = List.empty) extends HumanAction:
  override def execute: Unit =
    if (targets.isEmpty) println("City Defense failed: No targets specified")
    else println(s"City Defense activated on: ${targets.mkString(", ")}")

/** Concrete implementation: GlobalDefense */
case class GlobalDefense(targets: List[ActionTarget]= List.empty) extends HumanAction:
  override def execute: Unit =
    if (targets.isEmpty) println("Global Defense failed: No targets specified")
    else println(s"Global Defense deployed to: ${targets.mkString(", ")}")

/** Concrete implementation: DevelopKillSwitch (no targets) */
case object DevelopKillSwitch extends HumanAction:
  override def targets: List[ActionTarget] = List.empty
  override def execute: Unit = println("Kill Switch developed successfully")
*/