//package model.strategy
//
//import AiAbility.*
//import model.map.CityModule
//import model.strategy.ExecuteActionResult.ExecuteActionResult
//import model.strategy.PlayerAI.PlayerAI
//import model.strategy.playerActions.*
//
//import util.chaining.scalaUtilChainingOps
//import scala.util.Random
//
//
//object PlayerAI:
//  opaque type PlayerAI = PlayerAIImpl
//  
//  def default: PlayerAI = PlayerAIImpl()
//  //add here creation fromDifficulty
//
//  extension (ai: PlayerAI)
//    def executeAction(action: AiAction): ExecuteActionResult =
//      ai.executeAction(action)
//    def toString: String = ai.toString
//
//    /** Returns the set of abilities unlocked by the AI. */
//    def unlockedAbilities: Set[AiAbility] = ai.unlockedAbilities
//
//    /** Returns the list of actions executed by the AI. */
//    def executedActions: List[AiAction] = ai.executedActions
//
//    /** Returns the current infection chance percentage. */
//    def infectionChance: Int = ai.infectionChance
//
//    /** Returns the current sabotage power. */
//    def sabotagePower: Int = ai.sabotagePower
//
//    /** Returns the set of cities conquered by the AI. */
//    def conqueredCities: Set[String] = ai.conqueredCities
//
//    /** Returns the set of cities sabotaged by the AI. */
//    def sabotagedCities: Set[String] = ai.sabotagedCities
//  
//
///** Represents an AI-controlled player.
// *
// * @param unlockedAbilities Abilities the AI has unlocked.
// * @param executedActions   Actions the AI has already executed.
// * @param infectionChance   Current chance of infection success.
// * @param sabotagePower     Current power of sabotage actions.
// * @param conqueredCities   Cities that have been infected.
// * @param sabotagedCities   Cities that have been sabotaged.
// */
//private case class PlayerAIImpl (
//                     // possible extensions: base value should be inherited from the difficulty options
//                     unlockedAbilities : Set[AiAbility] = Set.empty,
//                     executedActions: List[AiAction] = List.empty,
//                     infectionChance: Int = 50, // base probability to infect
//                     sabotagePower: Int = 5, // need to decide,
//                     conqueredCities: Set[String] = Set.empty,
//                     sabotagedCities: Set[String] = Set.empty
//                   ) extends PlayerEntity:
//
//  override type ValidAction = AiAction
//
//  /** Executes a given AI action and updates the player's state. */
//  override def executeAction(action: ValidAction): ExecuteActionResult = doExecuteAction(action)
//
//  private def doExecuteAction(action: AiAction): ExecuteActionResult = action match
//    case _: EvolveAction => evolve
//    case a: InfectAction => this.infect(a.targets)
//    case a: SabotageAction => this.sabotage(a.targets)
//
//  /** String representation of the AI's current status. */
//  override def toString: String =
//    s"""|--- PlayerAI Status ---
//        |Unlocked Abilities   : ${if (unlockedAbilities.isEmpty) "None" else unlockedAbilities.mkString(", ")}
//        |Infection Chance     : $infectionChance%
//        |Sabotage Power       : $sabotagePower
//        |Conquered Cities     : ${if (conqueredCities.isEmpty) "None" else conqueredCities.mkString(", ")}
//        |Sabotaged Cities     : ${if (sabotagedCities.isEmpty) "None" else sabotagedCities.mkString(", ")}
//        |Executed Actions     :
//        |  ${if (executedActions.isEmpty) "None" else executedActions.map(_.execute).mkString("\n  ")}
//        |------------------------
//     """.stripMargin
//
//  /** Adds a new ability to the AI and adjusts stats accordingly. */
//  private def withNewAbility(ability: AiAbility): PlayerAIImpl = copy(
//    unlockedAbilities = unlockedAbilities + ability,
//    infectionChance = infectionChance + ability.infectionBonus,
//    sabotagePower = sabotagePower + ability.sabotageBonus
//  )
//
//  /** Unlocks a new random ability, if available. */
//  private def evolve: ExecuteActionResult =
//    AiAbility.allAbilities
//      .diff(unlockedAbilities)
//      .toList
//      .pipe(Random.shuffle)
//      .headOption
//      .fold(this)(withNewAbility)
//      .addAction(EvolveAction())
//      .pipe(updated => ExecuteActionResult.fromPlayerEntity(updated, None))
//  /** Conquers the given cities and adds the infect action to history. */
//  private def infect(cities: List[String]): ExecuteActionResult =
//    val updatedPlayer = copy(conqueredCities = conqueredCities ++ cities)
//    val updatedCity = CityModule.CityImpl.createCity(cities.head, 40).tryToInfectCity()
//    ExecuteActionResult.fromPlayerEntity(updatedPlayer, Some(updatedCity))
//
//
//  /** Sabotages the given cities and adds the sabotage action to history. */
//  private def sabotage(cities: List[String]): ExecuteActionResult =
//    val updatedPlayer = copy(sabotagedCities = sabotagedCities ++ cities)
//    val updatedCity = CityModule.CityImpl.createCity(cities.head, 40).tryToSabotateCity(sabotagePower)
//    ExecuteActionResult.fromPlayerEntity(updatedPlayer, Some(updatedCity))
//
//
//  /** Adds an executed action to the history. */
//  private def addAction(action: AiAction): PlayerAIImpl =
//    copy(executedActions = action :: executedActions)

package model.strategy

import AiAbility.*
import model.map.CityModule
import model.map.CityModule.*
import model.map.CityModule.CityImpl.City
import model.strategy.ExecuteActionResult.ExecuteActionResult
import model.strategy.playerActions.*

import util.chaining.scalaUtilChainingOps
import scala.util.Random

// 1. PlayerAI diventa un trait che estende PlayerEntity
trait PlayerAI extends PlayerEntity:
  // Definisci qui i membri pubblici del PlayerAI
  // Sposta i metodi dall'estensione dell'opaque type qui
  def unlockedAbilities: Set[AiAbility]
  def executedActions: List[AiAction]
  def infectionChance: Int
  def sabotagePower: Int
  def conqueredCities: Set[String]
  def sabotagedCities: Set[String]
  // Assicurati che il tipo associato ValidAction sia corretto
  override type ValidAction = AiAction // Specifica il bound superiore

  // Il metodo executeAction definito in PlayerEntity ora è astratto qui
  override def executeAction(action: ValidAction): ExecuteActionResult

  // Puoi anche definire un metodo toString qui se lo vuoi parte dell'interfaccia
  override def toString: String

// 3. Companion object per PlayerAI (per factory methods)
object PlayerAI:
  // Il metodo default crea un'istanza di PlayerAIImpl (che è un PlayerAI)
  def default: PlayerAI = PlayerAIImpl()
//add here creation fromDifficulty

// 2. PlayerAIImpl estende il trait PlayerAI
private case class PlayerAIImpl (
                                  unlockedAbilities : Set[AiAbility] = Set.empty,
                                  executedActions: List[AiAction] = List.empty,
                                  infectionChance: Int = 50, // base probability to infect
                                  sabotagePower: Int = 5, // need to decide,
                                  conqueredCities: Set[String] = Set.empty,
                                  sabotagedCities: Set[String] = Set.empty
                                ) extends PlayerAI: // Ora estende PlayerAI

  // 4. Implementa i membri definiti nel trait PlayerAI
  override type ValidAction = AiAction // Specifica il tipo concreto

  /** Executes a given AI action and updates the player's state. */
  // Implementa il metodo executeAction del trait PlayerAI
  override def executeAction(action: ValidAction): ExecuteActionResult = doExecuteAction(action)

  // Metodo interno per l'esecuzione delle azioni specifiche
  private def doExecuteAction(action: AiAction): ExecuteActionResult = action match
    case _: EvolveAction => evolve
    case a: InfectAction => this.infect(a.targets)
    case a: SabotageAction => this.sabotage(a.targets)

  /** String representation of the AI's current status. */
  // Implementa il metodo toString del trait PlayerAI
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
  private def evolve: ExecuteActionResult =
    val updatedPlayer: PlayerAIImpl = AiAbility.allAbilities
      .diff(unlockedAbilities)
      .toList
      .pipe(Random.shuffle)
      .headOption
      .fold(this)(withNewAbility)
      .addAction(EvolveAction())

    // Ora updatedPlayer (che è un PlayerAIImpl) può essere passato dove si aspetta PlayerAI
    // perché PlayerAIImpl estende PlayerAI
    ExecuteActionResult.fromPlayerEntity(updatedPlayer, None)


  /** Conquers the given cities and adds the infect action to history. */
  private def infect(cities: List[String]): ExecuteActionResult =
    val updatedPlayer: PlayerAIImpl = copy(conqueredCities = conqueredCities ++ cities).addAction(InfectAction(cities))
    // Assuming CityModule.CityImpl.createCity and tryToInfectCity work as expected
    val updatedCity: City = CityModule.CityImpl.createCity(cities.head, 40).tryToInfectCity()

    // updatedPlayer (PlayerAIImpl) può essere passato a fromPlayerEntity che aspetta PlayerAI
    ExecuteActionResult.fromPlayerEntity(updatedPlayer, Some(updatedCity))


  /** Sabotages the given cities and adds the sabotage action to history. */
  private def sabotage(cities: List[String]): ExecuteActionResult =
    val updatedPlayer: PlayerAIImpl = copy(sabotagedCities = sabotagedCities ++ cities).addAction(SabotageAction(cities))
    // Assuming CityModule.CityImpl.createCity and tryToSabotateCity work as expected
    val updatedCity: City = CityModule.CityImpl.createCity(cities.head, 40).tryToSabotateCity(sabotagePower)

    // updatedPlayer (PlayerAIImpl) può essere passato a fromPlayerEntity che aspetta PlayerAI
    ExecuteActionResult.fromPlayerEntity(updatedPlayer, Some(updatedCity))


  /** Adds an executed action to the history. */
  private def addAction(action: AiAction): PlayerAIImpl =
    copy(executedActions = action :: executedActions)