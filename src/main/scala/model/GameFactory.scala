package model

import model.map.WorldMapModule.{DeterministicMapModule, UndeterministicMapModule, WorldMap, createWorldMap}
import model.map.WorldState.*
import model.strategy.{PlayerAI, PlayerHuman}
import model.util.GameDifficulty.Difficulty

object GameFactory : 
  def createGame(difficulty: Difficulty = Difficulty.Normal) :  WorldState =
    createWorldState(createWorldMap(10)(DeterministicMapModule), PlayerAI.fromDifficulty(difficulty),  PlayerHuman.fromDifficulty(difficulty))