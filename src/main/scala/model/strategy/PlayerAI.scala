package model.strategy

import AiAbility.*
import model.strategy.playerActions.*

import util.chaining.scalaUtilChainingOps
import scala.util.Random



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

  override def executeAction(action: ValidAction): PlayerAI = doExecuteAction(action)

  private def doExecuteAction(action: AiAction): PlayerAI = action match
    case _: EvolveAction => evolve
    case action: InfectAction => this.infect(action.targets)
    case action: SabotageAction => this.sabotage(action.targets)
    case _ => this.addAction(action)

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

  private def withNewAbility(ability: AiAbility): PlayerAI = copy(
    unlockedAbilities = unlockedAbilities + ability,
    infectionChance = infectionChance + ability.infectionBonus,
    sabotagePower = sabotagePower + ability.sabotageBonus
  )

  private def evolve: PlayerAI =
    AiAbility.allAbilities
      .diff(unlockedAbilities)
      .toList
      .pipe(Random.shuffle)
      .headOption
      .fold(this)(withNewAbility)
      .addAction(EvolveAction())

  private def infect(cities: List[String]): PlayerAI =
    copy(conqueredCities = conqueredCities ++ cities)
      .addAction(InfectAction(cities))

  private def sabotage(cities: List[String]): PlayerAI =
    copy(sabotagedCities = sabotagedCities ++ cities)
      .addAction(SabotageAction(cities))

  private def addAction(action: AiAction): PlayerAI =
    copy(executedActions = action :: executedActions)
