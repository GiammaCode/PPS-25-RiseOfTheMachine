package model.strategy

import model.map.CityModule.CityImpl.City
import model.strategy.PlayerAI

object ExecuteActionResult:
  opaque type ExecuteActionResult[P <: PlayerEntity] = (P, Option[City], List[String])

  /** Creates an ExecuteActionResult for a given player of type P and optional city. */
  def apply[P <: PlayerEntity](player: P, city: Option[City], messages: List[String]): ExecuteActionResult[P] =
    (player, city, messages)

  /** Extension methods for the parameterized ExecuteActionResult. */
  extension [P <: PlayerEntity](res: ExecuteActionResult[P])
    def getPlayer: P = res._1
    def getCity: Option[City] = res._2
    def getMessages: List[String] = res._3