package model

import model.map.WorldMapModule.{UndeterministicMapModule, WorldMap, createWorldMap}
import model.map.WorldState.*
import model.strategy.{PlayerAI, PlayerHuman}

object GameFactory : 
  def createGame() :  WorldState =
    createWorldState(createWorldMap(10)(UndeterministicMapModule), PlayerAI.default,  PlayerHuman.default)