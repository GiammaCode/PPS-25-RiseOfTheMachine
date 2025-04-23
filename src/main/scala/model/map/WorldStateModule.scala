package model.map

import model.map.WorldMapModule.{WorldMap, createWorldMap}

object  WorldStateModule:

  opaque type WorldState = (Int, WorldMap)

  private var turn: Int = 0
  val worldMap: WorldMap = createWorldMap(10)
  val killSwitchPercentage: Int= 0

  private def currentTurn(): Int =
    increaseTurn()
    turn

  private def increaseTurn(): Unit =
    turn = turn + 1


  def IsGameOver(): Boolean = worldMap.numberOfCityInfected() > 10//worldMap.AIConquerPercetage > 70
  def updateState(): WorldState = (currentTurn(),worldMap.update()) //come input avremo le azione dell'IA e degli umani
  def getMap: WorldMap = worldMap


