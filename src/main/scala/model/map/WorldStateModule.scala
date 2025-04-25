package model.map
import model.map.WorldMapModule.*
import model.strategy.{AiAction, HumanAction, PlayerAI, PlayerHuman}

object  WorldStateModule:

  opaque type WorldState = (WorldMap,PlayerAI,PlayerHuman)

  private var turn: Int = 0
  val killSwitchPercentage: Int= 0

  def creteWorldState(): WorldState =
    (createWorldMap(10)(UndeterministicMapModule), PlayerAI.default ,PlayerHuman.default)
  private def currentTurn(): Int =
    increaseTurn()
    turn

  private def increaseTurn(): Unit =
    turn = turn + 1
  
  extension(worldState: WorldState)
    def IsGameOver(): Boolean = worldState._1.numberOfCityInfected() > 10//worldMap.AIConquerPercetage > 70
    def doHumanAction(action : HumanAction): Unit = ???
    def doPlayerAction(action: AiAction): Unit = ???
    def getMap: WorldMap = worldState._1




