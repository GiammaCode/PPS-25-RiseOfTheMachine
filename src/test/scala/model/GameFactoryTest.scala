package model

import model.map.CityModule.CityImpl.City
import model.map.WorldMapModule.WorldMap
import model.strategy.HumanActionTypes.CityDefense
import model.strategy.PlayerAI
import model.strategy.humanActions.CityDefenseAction
import model.strategy.playerActions.EvolveAction
import model.strategy.{AiAction, HumanAction, PlayerAI, PlayerHuman}
import org.junit.Assert.assertTrue
import org.junit.{Before, Test}


class GameFactoryTest :
  var worldState : (PlayerAI, PlayerHuman, WorldMap) = GameFactory.createGame()
  var aiAction : AiAction = _
  var humanAction : HumanAction = _
  @Before
    def initGame() : Unit =
    var worldState = GameFactory.createGame()

  @Test
  def applyModelMethodTest() : Unit =
    aiAction = EvolveAction()
    humanAction = CityDefenseAction()
    val updatedPlayer = worldState._1.executeAction(aiAction)
    //val updateHuman = worldState._2.executeAction(humanAction)
    assert(worldState._3.numberOfCityInfected() == 0)
    assert(updatedPlayer.getPlayer.unlockedAbilities.nonEmpty)
   // assertTrue(updateHuman.getPlayer.executedActions.nonEmpty)

