package model.strategy

import model.map.CityModule.CityImpl.City
import model.strategy.PlayerAI

object ExecuteActionResult:
  opaque type ExecuteActionResult = (PlayerAI, Option[City])
  
    def fromPlayerEntity(player: PlayerAI, city: Option[City]): ExecuteActionResult =
      (player, city)
    extension (res: ExecuteActionResult)
      def getPlayer: PlayerAI = res._1
      def getCity: Option[City] = res._2
      
      //TODO: need to be changed with a PlayerEntity instead of PlayerAIImpl, now is just to develop tests

  