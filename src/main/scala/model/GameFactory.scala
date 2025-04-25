package model

import model.map.WorldMapModule.{UndeterministicMapModule, WorldMap, createWorldMap}
import model.map.WorldStateModule.WorldState
import model.strategy.{PlayerAI, PlayerHuman}

object GameFactory : 
  def createGame() : (PlayerAI, PlayerHuman, WorldMap) =
    (PlayerAI.default , PlayerHuman(), createWorldMap(10)(UndeterministicMapModule)) //TODO: add creation method for player in common trait
    
    

