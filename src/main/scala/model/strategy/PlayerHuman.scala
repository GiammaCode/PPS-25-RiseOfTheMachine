package model.strategy

import  model.strategy.humanActions.*

case class PlayerHuman(
                      executedActions: List[HumanAction],
                      conqueredCities: Set[String] = Set("A", "B", "c"),
                      killSwitch: Int = 0,
                      defendedCities : Set[String]
                      ) extends PlayerEntity:

  override type ValidAction = HumanAction

  override def executeAction(action: ValidAction): PlayerHuman = doExecuteAction(action)

  private def doExecuteAction(action: HumanAction): PlayerHuman = ???


