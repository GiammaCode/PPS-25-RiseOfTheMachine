package model.strategy

import model.map.WorldState.WorldState


trait PlayerStrategy[A <: TurnAction]:
  def decideAction(state: WorldState): A

object SmartHumanStrategy extends PlayerStrategy[HumanAction]:
  override def decideAction(state: WorldState): HumanAction = ???