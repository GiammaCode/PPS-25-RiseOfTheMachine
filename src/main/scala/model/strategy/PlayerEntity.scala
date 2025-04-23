package model.strategy

trait PlayerEntity :
    type ValidAction <: TurnAction

    def executedActions: List[ValidAction]

    def conqueredCities: Set[String]

    def executeAction(action: ValidAction): PlayerEntity