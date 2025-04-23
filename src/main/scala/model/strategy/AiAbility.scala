package model.strategy

object AiAbility:
  sealed trait AiAbility:
    def infectionBonus: Int = 0
    def sabotageBonus: Int = 0

  case object ImprovedInfection extends AiAbility:
    override def infectionBonus: Int = 10

  case object StealthSabotage extends AiAbility:
    override def sabotageBonus: Int = 5

  val allAbilities : Set[AiAbility] = Set(ImprovedInfection, StealthSabotage)