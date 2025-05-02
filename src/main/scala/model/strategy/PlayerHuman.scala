package model.strategy

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.WorldMap
import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.strategy.HumanAction

trait PlayerHuman extends PlayerEntity:
  def killSwitch: Int

  def defendedCities: Set[String]

  def executedActions: List[HumanAction]

  def conqueredCities: Set[String]

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self]

  override def toString: String

object PlayerHuman:
  def default: PlayerHuman = PlayerHumanImpl()

private case class PlayerHumanImpl(
                                    killSwitch: Int = 0,
                                    defendedCities: Set[String] = Set.empty,
                                    conqueredCities: Set[String] = Set.empty,
                                    executedActions: List[HumanAction] = List.empty
                                  ) extends PlayerHuman:

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  override def executeAction(action: ValidAction,  worldMap: WorldMap): ExecuteActionResult[Self] = doExecuteAction(action, worldMap)

  private def doExecuteAction(action: HumanAction,  worldMap: WorldMap): ExecuteActionResult[Self] = action match
    case CityDefense(targets) =>
      val updated = copy(defendedCities = defendedCities ++ targets).addAction(action)
      val maybeCity = targets.headOption.map(cityName => worldMap.getCityByName(cityName)).map(_.defenseCity())
      ExecuteActionResult.fromPlayerEntity(updated, maybeCity)

    case GlobalDefense(targets) =>
      val updated = copy(defendedCities = defendedCities ++ targets).addAction(action)
      ExecuteActionResult.fromPlayerEntity(updated, None)

    case DevelopKillSwitch =>
      val updated = copy(killSwitch = killSwitch + 1).addAction(action)
      ExecuteActionResult.fromPlayerEntity(updated, None)

  private def addAction(action: HumanAction): PlayerHumanImpl =
    copy(executedActions = action :: executedActions)

  override def toString: String =
    s"""|--- Human Status ---
        |KillSwitch Progress : $killSwitch
        |Conquered Cities    : ${if (conqueredCities.isEmpty) "None" else conqueredCities.mkString(", ")}
        |Defended Cities     : ${if (defendedCities.isEmpty) "None" else defendedCities.mkString(", ")}
        |Executed Actions    :
        |  ${if (executedActions.isEmpty) "None" else executedActions.map(_.execute).mkString("\n  ")}
        |------------------------""".stripMargin