package model.strategy

import model.strategy.humanActions.*

object PlayerHuman:
  def apply: PlayerHuman = PlayerHuman()

case class PlayerHuman private(
                        executedActions: List[HumanAction] = List.empty,
                        conqueredCities: Set[String] = Set("A", "B", "c"),
                        killSwitch: Int = 0,
                        defendedCities: Set[String] = Set.empty
                      ) extends PlayerEntity:

  override type ValidAction = HumanAction

  override def executeAction(action: ValidAction): PlayerHuman =
    doExecuteAction(action)

  override def toString : String =
  s"""|--- Human Status ---
      |Conquered Cities     : ${if (conqueredCities.isEmpty) "None" else conqueredCities.mkString(", ")}
      |Defended Cities     : ${if (defendedCities.isEmpty) "None" else defendedCities.mkString(", ")}
      |Executed Actions     :
      |  ${if (executedActions.isEmpty) "None" else executedActions.map(_.execute).mkString("\n  ")}
      |------------------------
     """.stripMargin


  private def doExecuteAction(action: HumanAction): PlayerHuman = action match
    case action: CityDefenseAction => singleCityDefense(action.targets)
    case action: GlobalDefenseAction => globalDefense(action.targets)
    case action: DevelopKillSwitchAction => developKillSwitchAction

  private def singleCityDefense(city: List[String]): PlayerHuman =
    copy(defendedCities = defendedCities ++ city)
      .addAction(CityDefenseAction(city))

  private def globalDefense(cities: List[String]) =
    copy(defendedCities = defendedCities ++ cities)
      .addAction(GlobalDefenseAction(cities))

  private def developKillSwitchAction = addAction(DevelopKillSwitchAction())

  private def addAction(action: HumanAction): PlayerHuman =
    copy(executedActions = action :: executedActions)


