package model

class WorldState():

  val turn: Int = 0
  val worldMap: WorldMap = WorldMap(10)
  val killSwitchPercentage: Int= 0

  def IsGameOver(): Boolean = ???//worldMap.AIConquerPercetage > 70
  def updateState(): WorldState = ???


