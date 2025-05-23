package model.strategy

/** Represents actions specifically for human players */
sealed trait HumanAction extends TurnAction:
  def targets: List[ActionTarget]

  def name: String = this match
    case CityDefense(_) => "CityDefense"
    case GlobalDefense(_) => "GlobalDefense"
    case DevelopKillSwitch => "DevelopKillSwitch"


object HumanAction:
  val allActions: List[HumanAction] = List(CityDefense(), GlobalDefense(), DevelopKillSwitch)
  def cityDefense(targets: List[ActionTarget]) : HumanAction = CityDefense(targets)

  def globalDefense(targets: List[ActionTarget]): HumanAction = GlobalDefense(targets)

  def developKillSwitch(): HumanAction = DevelopKillSwitch

case class CityDefense(targets: List[ActionTarget] = List.empty) extends HumanAction

case class GlobalDefense(targets: List[ActionTarget] = List.empty) extends HumanAction

case object DevelopKillSwitch extends HumanAction:
  override def targets: List[ActionTarget] = List.empty