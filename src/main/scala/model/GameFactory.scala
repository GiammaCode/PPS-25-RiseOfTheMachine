package model

import model.map.WorldMapModule.{UndeterministicMapModule, WorldMap, createWorldMap}
import model.map.WorldState.*
import model.strategy.{PlayerAI, PlayerHuman}
import model.util.GameDifficulty.Difficulty

object GameFactory : 
  def createGame(difficulty: Difficulty = Difficulty.Normal) :  WorldState =
    createWorldState(createWorldMap(10)(UndeterministicMapModule), PlayerAI.fromDifficulty(difficulty),  PlayerHuman.default)