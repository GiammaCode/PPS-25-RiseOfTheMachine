package model.strategy

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.WorldMap
import model.map.WorldState.WorldState
import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.strategy.HumanAction
import model.util.GameSettings.{Difficulty, GameSettings, HumanStats}

trait PlayerHuman extends PlayerEntity:
  def killSwitch: Int

  def defendedCities: Set[String]

  def conqueredCities: Set[String]
  
  def getPossibleAction: List[HumanAction]

  def executedActions: List[HumanAction]

  def decideActionByStrategy(worldState: WorldState): HumanAction

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self]

  override def toString: String

object PlayerHuman:
  def fromStats(using stats: HumanStats): PlayerHuman =
    PlayerHumanImpl(killSwitch = stats.killSwitch)

  def fromSettings(using settings: GameSettings): PlayerHuman =
    PlayerHumanImpl(killSwitch = settings.human.killSwitch)


private case class PlayerHumanImpl(
                                    killSwitch: Int = 0,
                                    defendedCities: Set[String] = Set.empty,
                                    conqueredCities: Set[String] = Set.empty,
                                    executedActions: List[HumanAction] = List.empty
                                  ) extends PlayerHuman:

  private val CityDefenseBoost = 20
  private val GlobalDefenseBoost = 2
  private val KillSwitchIncrement = 10

  private val EasyModeProbabilities = ActionProbabilities(70, 30, 0)
  private val NormalModeProbabilities = ActionProbabilities(33, 33, 34)
  private val HardModeProbabilities = ActionProbabilities(40, 100, 100)

  override type ValidAction = HumanAction
  override type Self = PlayerHuman

  override def getPossibleAction: List[HumanAction] = HumanAction.allActions

  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self] = action match
    case CityDefense(targets) =>
      val updated = withDefendedCities(targets.toSet).addAction(action)
      val defendedCities: List[City] = targets.flatMap(cityName =>
        worldMap.getCityByName(cityName).map(_.defenseCity(CityDefenseBoost))
      )
      result(updated, Some(defendedCities), s"CityDefense on: ${targets.mkString(", ")}")

    case GlobalDefense(targets) =>
      val updated = withDefendedCities(defendedCities).addAction(action)
      val updatedCities = targets.flatMap(worldMap.getCityByName).map(_.defenseCity(GlobalDefenseBoost))
      result(updated, Some(updatedCities), "GlobalDefense executed")

    case DevelopKillSwitch =>
      val updated = copy(killSwitch = killSwitch + KillSwitchIncrement).addAction(action)
      result(updated, None, "KillSwitch progress increased")

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

  def decideActionByStrategy(worldState: WorldState): HumanAction =
    given ActionProbabilities = worldState.difficulty match
      case Difficulty.Easy => EasyModeProbabilities
      case Difficulty.Normal => NormalModeProbabilities
      case Difficulty.Hard => HardModeProbabilities

    SmartHumanStrategy.decideAction(worldState)

  private def result(player: PlayerHumanImpl, city: Option[List[City]], message: String): ExecuteActionResult[Self] =
    ExecuteActionResult(player, city, List(message))

  private def withDefendedCities(cities: Set[String]): PlayerHumanImpl =
    copy(defendedCities = defendedCities ++ cities)

  private def addAction(action: HumanAction): PlayerHumanImpl =
    copy(executedActions = action :: executedActions)







