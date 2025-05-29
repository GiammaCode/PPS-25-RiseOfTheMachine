package model.strategy

object AiAbilityValues:
  val ImprovedInfectionBonusPerc: Int = 50
  val StealthSabotageBonusPerc: Int = 50
  val DefaultPerc: Int = 0
/** Represents abilities that an AI player can unlock.
 * These abilities enhance the AI's infection or sabotage capabilities.
 */
object AiAbility:
  import AiAbilityValues._
  /** Trait for all AI abilities.
   * Provides optional bonuses to infection, sabotage, etc.
   */
  sealed trait AiAbility:

    /** Bonus to infection probability. Defaults to 0. */
    def infectionBonusPerc: Int = DefaultPerc
    /** Bonus to sabotage power. Defaults to 0. */
    def sabotageBonusPerc: Int = DefaultPerc

  /** Grants a +15% infection bonus when unlocked. */
  case object ImprovedInfection extends AiAbility:
    override def infectionBonusPerc: Int = ImprovedInfectionBonusPerc

  /** Grants a +15% sabotage bonus when unlocked. */
  case object StealthSabotage extends AiAbility:
    override def sabotageBonusPerc: Int = StealthSabotageBonusPerc

  /** Set of all available AI abilities. */
  val allAbilities : Set[AiAbility] = Set(ImprovedInfection, StealthSabotage) //Could be changed to a getAll