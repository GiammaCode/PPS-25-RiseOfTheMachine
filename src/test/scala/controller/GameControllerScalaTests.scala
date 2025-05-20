package controller

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import controller.GameController.*
import model.util.GameDifficulty.Difficulty
import model.util.GameMode.GameMode
import model.map.WorldState.WorldState
import model.strategy.AiAction

class GameControllerScalaTests extends AnyFunSuite with Matchers {
  given Difficulty = Difficulty.Easy
  given GameMode = GameMode.Singleplayer

  test("buildGameState should initialize a valid GameState") {
    val gameState = buildGameState
    gameState.worldState.playerAI should not be null
    gameState.worldState.playerHuman should not be null
    gameState.worldState.worldMap.numberOfCity() should be > 0
  }

  test("doPlayerAction should update the world state") {
    val gameState = buildGameState
    val initialState = gameState.worldState

    // Choose a city to infect
    val targetCity = initialState.worldMap.HumanCities.head

    val (updatedState,_) = gameTurn().run(gameState)
    val updatedCity = updatedState.worldState.worldMap.getCityByName(targetCity.getName).get

    updatedCity.getOwner shouldBe model.map.CityModule.Owner.AI
  }
}
