package model

import model.map.WorldMapModule.WorldMap
import model.strategy.HumanActionTypes.CityDefense
import model.strategy.PlayerAI.PlayerAI
import model.strategy.PlayerHuman.PlayerHuman
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
  def applyModelMethod() : Unit =
    aiAction = EvolveAction()
    humanAction = CityDefenseAction()
    val updatedPlayer = worldState._1.executeAction(aiAction)
    val updateHuman = worldState._2.executeAction(humanAction)
    assert(worldState._3.numberOfCityInfected() == 0)
    assert(updatedPlayer.unlockedAbilities.nonEmpty)
    assertTrue(updateHuman.executedActions.nonEmpty)

