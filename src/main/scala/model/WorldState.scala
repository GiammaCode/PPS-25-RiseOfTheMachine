package model

import model.WorldMap.{WorldMap, createWorldMap}

class WorldState():

  val turn: Int = 0
  val worldMap: WorldMap = createWorldMap(10)
  val killSwitchPercentage: Int= 0

  def IsGameOver(): Boolean = ???//worldMap.AIConquerPercetage > 70
  def updateState(): WorldState = ???


