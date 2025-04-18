package model.strategy

object Ability:
  sealed trait Ability
  case object ImprovedInfection extends Ability
  case object StealthSabotage extends Ability
  case object EvolutionBoost extends Ability
  
  val allAbilities : Set[Ability] = Set(ImprovedInfection, StealthSabotage, EvolutionBoost)