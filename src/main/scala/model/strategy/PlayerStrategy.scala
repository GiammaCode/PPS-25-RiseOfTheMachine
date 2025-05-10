package model.strategy

import model.map.WorldState.WorldState

import scala.util.Random


trait PlayerStrategy[A <: TurnAction]:
  def decideAction(state: WorldState): A

object SmartHumanStrategy extends PlayerStrategy[HumanAction]:
  override def decideAction(state: WorldState): HumanAction =
    //magari da cambiare, lui puo difendere tutte le citta ?
    //ora prendo la prima città non difesa tra quelle adiacenti,
    // esclude quelle gia difese
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