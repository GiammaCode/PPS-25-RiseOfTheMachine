package model.strategy

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.WorldMap
import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.util.GameDifficulty.{Difficulty, HumanStats, humanStatsFor}
import model.strategy.HumanAction

trait PlayerHuman extends PlayerEntity:
  def killSwitch: Int

  def defendedCities: Set[String]

  def conqueredCities: Set[String]

  def executedActions: List[HumanAction]

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self]

  override def toString: String

object PlayerHuman:
  def fromDifficulty(difficulty: Difficulty): PlayerHuman =
    val stats = humanStatsFor(difficulty)
    PlayerHumanImpl(killSwitch = stats.killSwitch)
    
  def fromStats(using stats: HumanStats): PlayerHuman =
    PlayerHumanImpl(killSwitch = stats.killSwitch)


private case class PlayerHumanImpl(
                                    killSwitch: Int = 0,
                                    defendedCities: Set[String] = Set.empty,
                                    conqueredCities: Set[String] = Set.empty,
                                    executedActions: List[HumanAction] = List.empty
                                  ) extends PlayerHuman:

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self] = action match
    case CityDefense(targets) =>
      val updated = withDefendedCities(targets.toSet).addAction(action)
      val maybeCity = targets.headOption.flatMap(worldMap.getCityByName).map(_.defenseCity())
      result(updated, maybeCity, s"CityDefense on: ${targets.mkString(", ")}")

    case GlobalDefense(targets) =>
      val updated = withDefendedCities(defendedCities).addAction(action)
      result(updated, None, "GlobalDefense executed")

    case DevelopKillSwitch =>
      val updated = copy(killSwitch = killSwitch + 10).addAction(action)
      result(updated, None, "KillSwitch progress increased")

  private def result(player: PlayerHumanImpl, city: Option[City], message: String): ExecuteActionResult[Self] =
    ExecuteActionResult(player, city, List(message))

  private def withDefendedCities(cities: Set[String]): PlayerHumanImpl =
    copy(defendedCities = defendedCities ++ cities)

  private def addAction(action: HumanAction): PlayerHumanImpl =
    copy(executedActions = action :: executedActions)

  override def toString: String =
    def formatSet(label: String, set: Set[String]): String =
      s"$label : ${if set.isEmpty then "None" else set.mkString(", ")}"

    def formatList(label: String, list: List[HumanAction]): String =
      s"$label :\n  ${if list.isEmpty then "None" else list.reverse.mkString("\n  ")}"

    s"""|--- PlayerHuman Status ---
        |KillSwitch Progress : $killSwitch
        |${formatSet("Defended Cities", defendedCities)}
        |${formatSet("Conquered Cities", conqueredCities)}
        |${formatList("Executed Actions", executedActions)}
        |------------------------""".stripMargin







