package model.strategy

object Ability:
  sealed trait Ability:
    def infectionBonus: Int = 0
    def sabotageBonus: Int = 0

  case object ImprovedInfection extends Ability:
    override def infectionBonus: Int = 10

  case object StealthSabotage extends Ability:
    override def sabotageBonus: Int = 5

  val allAbilities : Set[Ability] = Set(ImprovedInfection, StealthSabotage)