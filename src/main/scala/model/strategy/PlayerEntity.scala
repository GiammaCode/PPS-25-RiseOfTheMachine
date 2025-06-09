package model.strategy

import model.map.WorldMapModule.WorldMap
import model.strategy.ExecuteActionResult.ExecuteActionResult

/** Represents any entity(e.g., a Human or AI) that can perform actions in the game
  */
trait PlayerEntity :
    /** The type of actions this entity can perform. */
    type ValidAction <: TurnAction
    type Self <: PlayerEntity

    /**
     * Get all of the executed actions
     * @return All actions this entity has executed. */
    def executedActions: List[ValidAction]

    /**
     * Get all conquered cities.
     * @return All cities this entity has conquered. */
    def conqueredCities: Set[String]

    /** Executes a valid action,
     * @return the updated ExecutionActionResult. */
    def executeAction(action: ValidAction, worldMap: WorldMap): ExecuteActionResult[Self]