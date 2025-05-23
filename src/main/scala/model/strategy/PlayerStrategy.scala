package model.strategy

import model.map.WorldState.WorldState
import model.util.GameSettings.Difficulty

import scala.util.Random


trait PlayerStrategy[A <: TurnAction]:
  def decideAction(state: WorldState): A

object SmartHumanStrategy extends PlayerStrategy[HumanAction]:

  override def decideAction(state: WorldState): HumanAction =

    //strategia passiva
    def decideEasy: HumanAction =
      val possibleTargets = state.attackableCities.map(_._1).toList
      val cityToDefend = possibleTargets
        .filterNot(state.playerHuman.defendedCities.contains)
        .take(1)

      val actions = List(
        Option.when(cityToDefend.nonEmpty)(CityDefense(cityToDefend)),
        Some(GlobalDefense())
      ).flatten

      Random.shuffle(actions).head

    //strategia normale random su tutte le azioni
    def decideNormal: HumanAction =
      val possibleTargets = state.attackableCities.map(_._1).toList
      val cityToDefend = possibleTargets
        .filterNot(state.playerHuman.defendedCities.contains)
        .take(1)

      val actions = List(
        Option.when(cityToDefend.nonEmpty)(CityDefense(cityToDefend)),
        Option.when(state.playerHuman.killSwitch < 50)(DevelopKillSwitch),
        Some(GlobalDefense())
      ).flatten
      Random.shuffle(actions).head

    //proactive strategy
    def decideHard: HumanAction =
      val possibleTargets = state.attackableCities.toList
        .sortBy { case (_, infect, sabotage) => -(infect + sabotage) } // prioritize higher risk
        .map(_._1)

      val topCities = possibleTargets
        .filterNot(state.playerHuman.defendedCities.contains)
        .take(1)

      val actions = List(
        Option.when(topCities.nonEmpty)(CityDefense(topCities)),
        Option.when(state.playerHuman.killSwitch < 80)(DevelopKillSwitch),
        Some(GlobalDefense(possibleTargets))
      ).flatten
      //Random.shuffle(actions).head
      GlobalDefense(possibleTargets)

    state.difficulty match
      case Difficulty.Easy => decideEasy
      case Difficulty.Normal => decideNormal
      case Difficulty.Hard => decideHard
