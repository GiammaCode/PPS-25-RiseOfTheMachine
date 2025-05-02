package model.strategy

import model.map.CityModule.CityImpl.City
import model.strategy.PlayerAI

object ExecuteActionResult:
  opaque type ExecuteActionResult[P <: PlayerEntity] = (P, Option[City])

  /** Creates an ExecuteActionResult for a given player of type P and optional city. */
  def fromPlayerEntity[P <: PlayerEntity](player: P, city: Option[City]): ExecuteActionResult[P] =
    (player, city)

  /** Extension methods for the parameterized ExecuteActionResult. */
  extension [P <: PlayerEntity](res: ExecuteActionResult[P])
    def getPlayer: P = res._1
    def getCity: Option[City] = res._2