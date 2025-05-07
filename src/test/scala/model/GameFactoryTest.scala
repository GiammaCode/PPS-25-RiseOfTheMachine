package model

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.WorldMap
import model.map.WorldState.*
import model.strategy.{AiAction, CityDefense, Evolve, HumanAction, PlayerAI, PlayerHuman}
import org.junit.Assert.assertTrue
import org.junit.{Before, Test}


class GameFactoryTest :
  var worldState: WorldState = GameFactory.createGame()
  var aiAction : AiAction = _
  var humanAction : HumanAction = _
  @Before
    def initGame() : Unit =
    var worldState = GameFactory.createGame()

  @Test
  def applyModelMethodTest() : Unit =
    aiAction = Evolve
    humanAction = CityDefense()
    val updatedPlayer = worldState.playerAI.executeAction(aiAction, worldState.worldMap)
    //val updateHuman = worldState._2.executeAction(humanAction)
    assert(worldState.worldMap.numberOfCityInfected() == 0)
    assert(updatedPlayer.getPlayer.unlockedAbilities.nonEmpty)
   // assertTrue(updateHuman.getPlayer.executedActions.nonEmpty)

