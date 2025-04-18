package model.strategy

import model.strategy.turnAction.*
import Ability.*

import scala.util.Random
case class PlayerAI(
                     // possible extensions: base value should be inherited from the difficulty options
                     unlockedAbilities : Set[Ability] = Set.empty,
                     executedActions: List[TurnAction] = List.empty,
                     infectionChance: Int = 50, // base probability to infect
                     sabotagePower: Int = 5, // need to decide,
                     conqueredCities: Set[String] = Set.empty,
                     sabotagedCities: Set[String] = Set.empty
                   ):

  def executeAction(action: TurnAction) : PlayerAI =
    val updatedPlayer = action match
      case evolve : EvolveAction =>
        val maybeNewAbility = Random.shuffle(Ability.allAbilities.diff(unlockedAbilities).toList).headOption
        maybeNewAbility match
          case Some(newAbility) =>
            this.copy(
              unlockedAbilities = unlockedAbilities + newAbility,
              infectionChance = infectionChance + newAbility.infectionBonus,
              sabotagePower = sabotagePower + newAbility.sabotageBonus
            )
          case None => this

      case infect : InfectAction => val newCities = infect.targets.getOrElse(Nil)
        this.copy(conqueredCities = conqueredCities ++ newCities)

      case sabotage : SabotageAction => val newCities = sabotage.targets.getOrElse(Nil)
        this.copy(sabotagedCities = sabotagedCities ++ newCities)

    updatedPlayer.copy(executedActions = action :: executedActions)
