package model.strategy

import AiAbility.*
import model.strategy.playerActions.*

import util.chaining.scalaUtilChainingOps
import scala.util.Random


/** Represents an AI-controlled player.
 *
 * @param unlockedAbilities Abilities the AI has unlocked.
 * @param executedActions   Actions the AI has already executed.
 * @param infectionChance   Current chance of infection success.
 * @param sabotagePower     Current power of sabotage actions.
 * @param conqueredCities   Cities that have been infected.
 * @param sabotagedCities   Cities that have been sabotaged.
 */
case class PlayerAI(
                     // possible extensions: base value should be inherited from the difficulty options
                     unlockedAbilities : Set[AiAbility] = Set.empty,
                     executedActions: List[AiAction] = List.empty,
                     infectionChance: Int = 50, // base probability to infect
                     sabotagePower: Int = 5, // need to decide,
                     conqueredCities: Set[String] = Set.empty,
                     sabotagedCities: Set[String] = Set.empty
                   ) extends PlayerEntity:

  override type ValidAction = AiAction

  /** Executes a given AI action and updates the player's state. */
  override def executeAction(action: ValidAction): PlayerAI = doExecuteAction(action)

  private def doExecuteAction(action: AiAction): PlayerAI = action match
    case _: EvolveAction => evolve
    case action: InfectAction => this.infect(action.targets)
    case action: SabotageAction => this.sabotage(action.targets)

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
  private def withNewAbility(ability: AiAbility): PlayerAI = copy(
    unlockedAbilities = unlockedAbilities + ability,
    infectionChance = infectionChance + ability.infectionBonus,
    sabotagePower = sabotagePower + ability.sabotageBonus
  )

  /** Unlocks a new random ability, if available. */
  private def evolve: PlayerAI =
    AiAbility.allAbilities
      .diff(unlockedAbilities)
      .toList
      .pipe(Random.shuffle)
      .headOption
      .fold(this)(withNewAbility)
      .addAction(EvolveAction())

  /** Conquers the given cities and adds the infect action to history. */
  private def infect(cities: List[String]): PlayerAI =
    copy(conqueredCities = conqueredCities ++ cities)
      .addAction(InfectAction(cities))

  /** Sabotages the given cities and adds the sabotage action to history. */
  private def sabotage(cities: List[String]): PlayerAI =
    copy(sabotagedCities = sabotagedCities ++ cities)
      .addAction(SabotageAction(cities))

  /** Adds an executed action to the history. */
  private def addAction(action: AiAction): PlayerAI =
    copy(executedActions = action :: executedActions)
