package model.strategy

/** Represents any entity(e.g., a Human or AI) that can perform actions in the game
  */
trait PlayerEntity :
    /** The type of actions this entity can perform. */
    type ValidAction <: TurnAction

    /** All actions this entity has executed. */
    def executedActions: List[ValidAction]

    /** All cities this entity has conquered. */
    def conqueredCities: Set[String]

    /** Executes a valid action, returning the updated player entity. */
    def executeAction(action: ValidAction): PlayerEntity