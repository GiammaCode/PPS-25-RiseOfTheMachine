package model.strategy

/** Represents actions specifically for human players */
sealed trait HumanAction extends TurnAction:
  def targets: List[Target]
  def execute: Unit

object HumanAction:
  def cityDefense(targets: List[Target]): HumanAction = CityDefense(targets)
  def globalDefense(targets: List[Target]): HumanAction = GlobalDefense(targets)
  def developKillSwitch(): HumanAction = DevelopKillSwitch

/** Concrete implementation: CityDefense */
case class CityDefense(targets: List[Target]) extends HumanAction:
  override def execute: Unit =
    if (targets.isEmpty) println("City Defense failed: No targets specified")
    else println(s"City Defense activated on: ${targets.mkString(", ")}")

/** Concrete implementation: GlobalDefense */
case class GlobalDefense(targets: List[Target]) extends HumanAction:
  override def execute: Unit =
    if (targets.isEmpty) println("Global Defense failed: No targets specified")
    else println(s"Global Defense deployed to: ${targets.mkString(", ")}")

/** Concrete implementation: DevelopKillSwitch (no targets) */
case object DevelopKillSwitch extends HumanAction:
  override def targets: List[Target] = List.empty
  override def execute: Unit = println("Kill Switch developed successfully")
