package model.strategy

case class PlayerAI(
                   // possible extensions: base value should be inherited from the difficulty options
                   // TODO: add list of the unlocked ability
                   executedActions: List[TurnAction] = List.empty,
                   infectionChance: Double = 0.5, // base probability to infect
                   sabotagePower: Double = 0.1, // need to decide,
                   conqueredCities: Set[String] = Set.empty
                   )
