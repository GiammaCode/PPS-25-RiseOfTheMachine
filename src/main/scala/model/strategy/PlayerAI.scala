package model.strategy

import model.map.{CityModule, WorldMapModule}
import model.map.CityModule.*
import model.map.CityModule.CityImpl.{City, createCity}
import model.map.WorldMapModule.WorldMap
import model.strategy.AiAbility.AiAbility
import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.util.GameSettings
import model.util.GameSettings.{AIStats, Difficulty, GameSettings, given}

import util.chaining.scalaUtilChainingOps
import scala.util.Random

/**
 * Represents the AI-controlled player in the game.
 * Handles execution of AI-specific actions such as infecting, sabotaging, and evolving.
 */
trait PlayerAI extends PlayerEntity:
  /**
   * The set of currently unlocked abilities by the AI.
   */
  def unlockedAbilities: Set[AiAbility]

  /**
   * A chronological list of actions the AI has executed.
   */
  def executedActions: List[AiAction]

  /**
   * The current chance (percentage) of successfully infecting a city.
   */
  def infectionPower: Int

  /**
   * The current power level of sabotage actions.
   */
  def sabotagePower: Int

  /**
   * The set of cities that have been conquered via infection.
   */
  def conqueredCities: Set[String]

  /**
   * The set of cities that have been sabotaged.
   */
  def sabotagedCities: Set[String]

  /**
   * Computes the list of valid AI actions based on the current state.
   */
  def getPossibleAction: List[AiAction]

  override type ValidAction = AiAction
  override type Self = PlayerAI

  /**
   * Executes a given action and returns the result, including updates to state and city (if any).
   *
   * @param action    The AI action to execute.
   * @param worldMap  The current world map context.
   * @return A result object containing the updated AI state, affected city (if any), and log messages.
   */
  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self]

  override def toString: String

/**
 * Companion object for the PlayerAI trait.
 */
object PlayerAI:

  /**
   * Constructs a PlayerAI instance using given AI statistics.
   *
   * @param stats Implicit AIStats (e.g. based on game difficulty).
   * @return A new PlayerAI instance.
   */
  def fromStats(using stats: AIStats): PlayerAI =
    PlayerAIImpl(infectionPower = stats.infectionPower, sabotagePower = stats.sabotagePower)

  /**
   * Constructs a PlayerAI instance using a complete GameSettings context.
   *
   * @param settings Implicit GameSettings containing AI configuration.
   * @return A new PlayerAI instance.
   */
  def fromSettings(using settings: GameSettings): PlayerAI =
    PlayerAIImpl(
      infectionPower = settings.ai.infectionPower,
      sabotagePower = settings.ai.sabotagePower
    )

/**
 * Concrete implementation of the PlayerAI trait.
 *
 * @param unlockedAbilities The set of AI abilities unlocked so far.
 * @param executedActions   The list of actions the AI has taken.
 * @param infectionPower   Probability to infect a city.
 * @param sabotagePower     Power level for sabotaging cities.
 * @param conqueredCities   Set of names of conquered cities.
 * @param sabotagedCities   Set of names of sabotaged cities.
 */
private case class PlayerAIImpl (
                                  unlockedAbilities : Set[AiAbility] = Set.empty,
                                  executedActions: List[AiAction] = List.empty,
                                  infectionPower: Int,
                                  sabotagePower: Int,
                                  conqueredCities: Set[String] = Set.empty,
                                  sabotagedCities: Set[String] = Set.empty
                                ) extends PlayerAI:

  override type ValidAction = AiAction
  override type Self = PlayerAI

  /** @inheritdoc */
  override def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self] =
    doExecuteAction(action, worldMap)

  /**
   * Internal dispatcher for executing AI actions.
   */
  private def doExecuteAction(action: AiAction, worldMap: WorldMap): ExecuteActionResult[Self] = action match
    case Infect(targets) => infect(targets, worldMap)
    case Sabotage(targets) => sabotage(targets, worldMap)
    case Evolve => evolve

  /** @inheritdoc */
  override def getPossibleAction: List[AiAction] =
    AiAction.allActions.filter { action =>
      (AiAbility.allAbilities -- unlockedAbilities).nonEmpty || action != Evolve
    }

  /** @inheritdoc */
  override def toString: String =
    s"""|--- PlayerAI Status ---
        |Unlocked Abilities   : ${if (unlockedAbilities.isEmpty) "None" else unlockedAbilities.mkString(", ")}
        |Infection Chance     : $infectionPower%
        |Sabotage Power       : $sabotagePower
        |Conquered Cities     : ${if (conqueredCities.isEmpty) "None" else conqueredCities.mkString(", ")}
        |Sabotaged Cities     : ${if (sabotagedCities.isEmpty) "None" else sabotagedCities.mkString(", ")}
        |Executed Actions     :
        |  ${if (executedActions.isEmpty) "None" else executedActions.reverse.mkString("\n  ")}
        |------------------------
     """.stripMargin

  /**
   * Adds a newly unlocked ability to the AI, updating its stats.
   *
   * @param ability The ability to unlock.
   * @return A new updated PlayerAIImpl instance.
   */
  private def withNewAbility(ability: AiAbility): PlayerAIImpl = copy(
    unlockedAbilities = unlockedAbilities + ability,
    infectionPower = math.round(infectionPower * (1 + ability.infectionBonusPerc / 100.0)).toInt,
    sabotagePower = math.round(sabotagePower * (1 + ability.sabotageBonusPerc / 100.0)).toInt
  )

  /**
   * Performs the evolve action, unlocking a new ability if available.
   */
  private def evolve: ExecuteActionResult[Self] =
    val updatedPlayer: PlayerAIImpl = AiAbility.allAbilities
      .diff(unlockedAbilities)
      .toList
      .pipe(Random.shuffle)
      .headOption
      .fold(this)(withNewAbility)
      .addAction(Evolve)
    ExecuteActionResult.apply(updatedPlayer, None, List(s"Evolve, you have unlocked: $unlockedAbilities\n"))

  /**
   * Performs the infect action on a list of city names.
   *
   * @param cities    The list of target cities.
   * @param worldMap  The game map context.
   * @return Execution result containing updated state and possibly infected city.
   */
  private def infect(cities: List[String], worldMap: WorldMap): ExecuteActionResult[Self] =
    val updatedPlayer: PlayerAIImpl = copy(conqueredCities = conqueredCities ++ cities).addAction(Infect(cities))
    val infectedCities: List[City] = cities.flatMap(cityName =>
      worldMap.getCityByName(cityName).map(_.infectCity())
    )
    ExecuteActionResult.apply(updatedPlayer, Some(infectedCities), List(s"Infect, you have infected: $conqueredCities\n"))

  /**
   * Performs the sabotage action on a list of city names.
   *
   * @param cities    The list of target cities.
   * @param worldMap  The game map context.
   * @return Execution result containing updated state and possibly sabotaged city.
   */
  private def sabotage(cities: List[String], worldMap: WorldMap): ExecuteActionResult[Self] =
    val updatedPlayer: PlayerAIImpl = copy(sabotagedCities = sabotagedCities ++ cities).addAction(Sabotage(cities))
    val sabotagedCityList: List[City] = cities.flatMap(cityName =>
      worldMap.getCityByName(cityName).map(_.sabotateCity(sabotagePower))
    )
    ExecuteActionResult.apply(updatedPlayer, Some(sabotagedCityList), List(s"Sabotage, you have sabotaged: $sabotagedCities\n"))

  /**
   * Adds an executed action to the internal history.
   *
   * @param action The action to log.
   * @return Updated PlayerAIImpl with action added.
   */
  private def addAction(action: AiAction): PlayerAIImpl =
    copy(executedActions = action :: executedActions)
