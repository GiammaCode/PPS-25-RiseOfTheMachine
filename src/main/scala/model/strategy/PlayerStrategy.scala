package model.strategy

import model.map.WorldState.WorldState
import model.util.GameDifficulty.Difficulty

import scala.util.Random


trait PlayerStrategy[A <: TurnAction]:
  def decideAction(state: WorldState): A

object SmartHumanStrategy extends PlayerStrategy[HumanAction]:

  override def decideAction(state: WorldState): HumanAction =
    val possibleTargets = state.attackableCities.map(_._1).toList
    val cityToDefend = possibleTargets
      .filterNot(state.playerHuman.defendedCities.contains)
      .take(1)

    val actions = List(
      Option.when(cityToDefend.nonEmpty)(CityDefense(cityToDefend)),
      Some(GlobalDefense())
    ).flatten

    Random.shuffle(actions).head