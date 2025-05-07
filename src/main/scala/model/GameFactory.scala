package model

import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, createWorldMap, given}
import model.map.WorldState.*
import model.strategy.{PlayerAI, PlayerHuman}


object GameFactory :
  def createGame() :  WorldState =
    createWorldState(createWorldMap(10)(DeterministicMapModule), PlayerAI.default,  PlayerHuman.default)