package model.strategy

import model.map.WorldState.WorldState
import model.util.GameDifficulty.Difficulty

import scala.util.Random


trait PlayerStrategy[A <: TurnAction]:
  def decideAction(state: WorldState): A

object SmartHumanStrategy extends PlayerStrategy[HumanAction]:

  override def decideAction(state: WorldState): HumanAction =
    state.difficulty match
      case Difficulty.Easy => decideEasy
      case Difficulty.Normal => decideNormal
      case Difficulty.Hard => decideHard

    //nested method aiutano la helper logic
    def decideEasy: HumanAction =
      val possibleTargets = state.attackableCities.map(_._1).toList
      val cityToDefend = possibleTargets.
        filterNot(state.playerHuman.defendedCities.contains).
        take(1)
      val possibleActions: List[HumanAction] =
        List(
          Option.when(cityToDefend.nonEmpty)(CityDefense(cityToDefend)),
          Option.when(state.playerHuman.killSwitch < 50)(DevelopKillSwitch),
          Some(GlobalDefense())
        ).flatten
      Random.shuffle(possibleActions).head

    def decideNormal: HumanAction = ???
    def decideHard: HumanAction = ???