package model.strategy

import  model.strategy.humanActions.*

case class PlayerHuman(
                      executedActions: List[HumanAction],
                      conqueredCities: Set[String] = Set("A", "B", "c"),
                      killSwitch: Int = 0,
                      defendedCities : Set[String]
                      ) extends PlayerEntity:

  override type ValidAction = HumanAction

  override def executeAction(action: ValidAction): PlayerHuman =

    doExecuteAction(action)

  private def doExecuteAction(action: HumanAction): PlayerHuman = action match
    case action: CityDefenseAction => singleCityDefense(action.targets)
    case action: GlobalDefenseAction => globalDefense(action.targets)
    case action: DevelopKillSwitchAction => developKillSwitchAction


  private def singleCityDefense(city: List[String]) : PlayerHuman =
    copy(defendedCities = defendedCities ++ city)
      .addAction(CityDefenseAction(city))

  private def globalDefense(cities: List[String]) =
    copy(defendedCities = defendedCities ++ cities)
      .addAction(GlobalDefenseAction(cities))

  private def developKillSwitchAction = addAction(DevelopKillSwitchAction())

  private def addAction(action: HumanAction): PlayerHuman =
    copy(executedActions = action :: executedActions)

