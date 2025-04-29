package model.strategy

/** Represents abilities that an AI player can unlock.
 * These abilities enhance the AI's infection or sabotage capabilities.
 */
object AiAbility:

  /** Trait for all AI abilities.
   * Provides optional bonuses to infection, sabotage, etc.
   */
  sealed trait AiAbility:
    /** Bonus to infection probability. Defaults to 0. */
    def infectionBonus: Int = 0
    /** Bonus to sabotage power. Defaults to 0. */
    def sabotageBonus: Int = 0

  /** Grants a +10 infection bonus when unlocked. */
  case object ImprovedInfection extends AiAbility:
    override def infectionBonus: Int = 10

  /** Grants a +5 sabotage bonus when unlocked. */
  case object StealthSabotage extends AiAbility:
    override def sabotageBonus: Int = 5

  /** Set of all available AI abilities. */
  val allAbilities : Set[AiAbility] = Set(ImprovedInfection, StealthSabotage) //Could be changed to a getAll