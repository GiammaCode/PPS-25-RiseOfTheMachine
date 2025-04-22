package model.map

import model.map.WorldMap.{WorldMap, createWorldMap}

object  WorldStateModule:
  type WorldState

  val turn: Int = 0
  val worldMap: WorldMap = createWorldMap(10)
  val killSwitchPercentage: Int= 0

  def IsGameOver(): Boolean = ???//worldMap.AIConquerPercetage > 70
  def updateState(): WorldState = ???


