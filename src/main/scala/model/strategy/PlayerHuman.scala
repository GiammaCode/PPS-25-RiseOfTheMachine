package model.strategy

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.WorldMap
import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.util.GameDifficulty.{Difficulty, humanStatsFor}
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
  def fromDifficulty(difficulty: Difficulty) : PlayerHuman =
    val stats = humanStatsFor(difficulty)
    PlayerHumanImpl(killSwitch = stats.killSwitch)


private case class PlayerHumanImpl(
                                    killSwitch: Int = 0,
                                    defendedCities: Set[String] = Set.empty,
                                    conqueredCities: Set[String] = Set.empty,
                                    executedActions: List[HumanAction] = List.empty
                                  ) extends PlayerHuman:

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  override def executeAction(action: ValidAction,  worldMap: WorldMap): ExecuteActionResult[Self] =
    doExecuteAction(action, worldMap)

  private def doExecuteAction(action: HumanAction,  worldMap: WorldMap): ExecuteActionResult[Self] = action match
    case CityDefense(targets) =>
      val updated = copy(defendedCities = defendedCities ++ targets).addAction(action)
      val maybeCity = for {
        cityName <- targets.headOption
        city <- worldMap.getCityByName(cityName) // ora restituisce Option
      } yield city.defenseCity()
      ExecuteActionResult.apply(updated, maybeCity, List("Defence"))

    case GlobalDefense(targets) =>
      val updated = copy(defendedCities = defendedCities ++ targets).addAction(action)
      ExecuteActionResult.apply(updated, None, List("Global defence"))

    case DevelopKillSwitch =>
      val updated = copy(killSwitch = killSwitch + 1).addAction(action)
      ExecuteActionResult.apply(updated, None, List("Develop kill switch"))

  private def addAction(action: HumanAction): PlayerHumanImpl =
    copy(executedActions = action :: executedActions)

  override def toString: String =
    s"""|--- PlayerHuman Status ---
        |KillSwitch Progress : $killSwitch
        |Defended Cities     : ${if defendedCities.isEmpty then "None" else defendedCities.mkString(", ")}
        |Conquered Cities    : ${if conqueredCities.isEmpty then "None" else conqueredCities.mkString(", ")}
        |Executed Actions    :
        |  ${if executedActions.isEmpty then "None" else executedActions.reverse.mkString("\n  ")}
        |------------------------""".stripMargin







