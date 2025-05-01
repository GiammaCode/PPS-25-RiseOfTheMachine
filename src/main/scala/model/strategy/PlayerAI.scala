
package model.strategy

import AiAbility.*
import model.map.{CityModule, WorldMapModule}
import model.map.CityModule.*
import model.map.CityModule.CityImpl.{City, createCity}
import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.strategy.playerActions.*

import util.chaining.scalaUtilChainingOps
import scala.util.Random

trait PlayerAI extends PlayerEntity:
  def unlockedAbilities: Set[AiAbility]
  def executedActions: List[AiAction]
  def infectionChance: Int
  def sabotagePower: Int
  def conqueredCities: Set[String]
  def sabotagedCities: Set[String]
  override type ValidAction = AiAction
  override type Self = PlayerAI

  override def executeAction(action: ValidAction): ExecuteActionResult[Self]

  override def toString: String

object PlayerAI:
  def default: PlayerAI = PlayerAIImpl()
//add here creation fromDifficulty

private case class PlayerAIImpl (
                                  unlockedAbilities : Set[AiAbility] = Set.empty,
                                  executedActions: List[AiAction] = List.empty,
                                  infectionChance: Int = 50, // base probability to infect
                                  sabotagePower: Int = 5, // need to decide,
                                  conqueredCities: Set[String] = Set.empty,
                                  sabotagedCities: Set[String] = Set.empty
                                ) extends PlayerAI: // Ora estende PlayerAI

  override type ValidAction = AiAction
  override type Self = PlayerAI

  /** Executes a given AI action and updates the player's state. */
  override def executeAction(action: ValidAction): ExecuteActionResult[Self] = doExecuteAction(action)

  private def doExecuteAction(action: AiAction): ExecuteActionResult[Self] = action match
    case _: EvolveAction => evolve
    case a: InfectAction => this.infect(a.targets)
    case a: SabotageAction => this.sabotage(a.targets)

  /** String representation of the AI's current status. */
  override def toString: String =
    s"""|--- PlayerAI Status ---
        |Unlocked Abilities   : ${if (unlockedAbilities.isEmpty) "None" else unlockedAbilities.mkString(", ")}
        |Infection Chance     : $infectionChance%
        |Sabotage Power       : $sabotagePower
        |Conquered Cities     : ${if (conqueredCities.isEmpty) "None" else conqueredCities.mkString(", ")}
        |Sabotaged Cities     : ${if (sabotagedCities.isEmpty) "None" else sabotagedCities.mkString(", ")}
        |Executed Actions     :
        |  ${if (executedActions.isEmpty) "None" else executedActions.map(_.execute).mkString("\n  ")}
        |------------------------
     """.stripMargin

  /** Adds a new ability to the AI and adjusts stats accordingly. */
  private def withNewAbility(ability: AiAbility): PlayerAIImpl = copy(
    unlockedAbilities = unlockedAbilities + ability,
    infectionChance = infectionChance + ability.infectionBonus,
    sabotagePower = sabotagePower + ability.sabotageBonus
  )

  /** Unlocks a new random ability, if available. */
  private def evolve: ExecuteActionResult[Self] =
    val updatedPlayer: PlayerAIImpl = AiAbility.allAbilities
      .diff(unlockedAbilities)
      .toList
      .pipe(Random.shuffle)
      .headOption
      .fold(this)(withNewAbility)
      .addAction(EvolveAction())
    ExecuteActionResult.fromPlayerEntity(updatedPlayer, None)


  /** Conquers the given cities and adds the infect action to history. */
  private def infect(cities: List[String]): ExecuteActionResult[Self] =
    val updatedPlayer: PlayerAIImpl = copy(conqueredCities = conqueredCities ++ cities).addAction(InfectAction(cities))
    val updatedCity: City = createCity(cities.head,4,false).infectCity()
    ExecuteActionResult.fromPlayerEntity(updatedPlayer, Some(updatedCity))


  /** Sabotages the given cities and adds the sabotage action to history. */
  private def sabotage(cities: List[String]): ExecuteActionResult[Self] =
    val updatedPlayer: PlayerAIImpl = copy(sabotagedCities = sabotagedCities ++ cities).addAction(SabotageAction(cities))
    val updatedCity: City = createCity(cities.head, 40,false).sabotateCity()
    ExecuteActionResult.fromPlayerEntity(updatedPlayer, Some(updatedCity))


  /** Adds an executed action to the history. */
  private def addAction(action: AiAction): PlayerAIImpl =
    copy(executedActions = action :: executedActions)