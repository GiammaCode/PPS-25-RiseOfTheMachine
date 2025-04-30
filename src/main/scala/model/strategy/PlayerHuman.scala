package model.strategy

import model.strategy.humanActions.*

object PlayerHuman:
  opaque type PlayerHuman = PlayerHumanImpl

  def default: PlayerHuman = PlayerHumanImpl()

  extension (human: PlayerHuman)
    def executeAction(action: HumanAction): PlayerHuman =
      human.executeAction(action)

    def executedActions: List[HumanAction] = human.executedActions

    def KillSwitch: Int = human.killSwitch

    def defendedCities: Set[String] = human.defendedCities

    def conqueredCities: Set[String] = human.conqueredCities


private case class PlayerHumanImpl(
                                    executedActions: List[HumanAction] = List.empty,
                                    conqueredCities: Set[String] = Set("A", "B", "c"),
                                    killSwitch: Int = 0,
                                    defendedCities: Set[String] = Set.empty
                                  ) extends PlayerEntity:


  override type ValidAction = HumanAction

  override def executeAction(action: ValidAction): PlayerHumanImpl =
    doExecuteAction(action)

  private def doExecuteAction(action: HumanAction): PlayerHumanImpl = action match
    case action: CityDefenseAction => singleCityDefense(action.targets)
    case action: GlobalDefenseAction => globalDefense(action.targets)
    case action: DevelopKillSwitchAction => developKillSwitchAction

  override def toString: String =
    s"""|--- Human Status ---
        |Conquered Cities     : ${if (conqueredCities.isEmpty) "None" else conqueredCities.mkString(", ")}
        |Defended Cities     : ${if (defendedCities.isEmpty) "None" else defendedCities.mkString(", ")}
        |Executed Actions     :
        |  ${if (executedActions.isEmpty) "None" else executedActions.map(_.execute).mkString("\n  ")}
        |------------------------
     """.stripMargin

  /**
   * Executes a city defense on a single city.
   */
  private def singleCityDefense(city: List[String]): PlayerHumanImpl =
    copy(defendedCities = defendedCities ++ city)
      .addAction(CityDefenseAction(city))

  /**
   * Executes a city defense on a single city.
   */
  private def globalDefense(cities: List[String]): PlayerHumanImpl =
    copy(defendedCities = defendedCities ++ cities)
      .addAction(GlobalDefenseAction(cities))

  /**
   * Executes the kill switch development action.
   */
  private def developKillSwitchAction: PlayerHumanImpl =
    addAction(DevelopKillSwitchAction())

  /**
   * Adds an executed action to the action history.
   */
  private def addAction(action: HumanAction): PlayerHumanImpl =
    copy(executedActions = action :: executedActions)


