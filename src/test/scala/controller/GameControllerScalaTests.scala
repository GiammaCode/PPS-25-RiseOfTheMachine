package controller

import controller.GameController.*
import model.util.GameDifficulty.Difficulty
import model.util.GameMode.GameMode
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameControllerScalaTests extends AnyFunSuite with Matchers {
  given Difficulty = Difficulty.Easy
  given GameMode = GameMode.Singleplayer
  val gameState: GameState = buildGameState()


  test("buildGameState should initialize a valid GameState") {
    gameState.worldState.playerAI should not be null
    gameState.worldState.playerHuman should not be null
    gameState.worldState.worldMap.numberOfCity() should be > 0
  }

  test("doPlayerAction should update the world state") {
    val initialState = gameState.worldState

    // Choose a city to infect
    val targetCity = initialState.worldMap.HumanCities.head

    val (updatedState,_) = gameTurn().run(gameState)
    val updatedCity = updatedState.worldState.worldMap.getCityByName(targetCity.getName).get

    updatedCity.getOwner shouldBe model.map.CityModule.Owner.AI
  }
}
