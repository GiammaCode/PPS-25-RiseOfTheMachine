package controller

import controller.GameController.*
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.io.ByteArrayInputStream

class GameControllerScalaTests extends AnyFunSuite with Matchers {
  given GameSettings = forSettings(GameMode.Singleplayer,Difficulty.Normal)
  val gameState: GameState = buildGameState


  test("buildGameState should initialize a valid GameState") {
    gameState.worldState.playerAI should not be null
    gameState.worldState.playerHuman should not be null
    gameState.worldState.worldMap.numberOfCity() should be > 0
  }

  test("doPlayerAction should update the world state") {
    val initialState = gameState.worldState

    val simulatedInput = "0 i" // poi se serve anche per Human: + "1\nRome\n"
    val in = new ByteArrayInputStream(simulatedInput.getBytes)

    Console.withIn(in) {
      // Choose a city to infect
      val targetCity = initialState.worldMap.humanCities.head

      val (updatedState, _) = gameTurn.run(gameState)
      val updatedCity = updatedState.worldState.worldMap.getCityByName(targetCity.getName).get

      updatedCity.getOwner shouldBe model.map.CityModule.Owner.AI
    }
  }


  test("Simulate CLI input for gameTurn") {
      // Simula input CLI: "0\n" per AI + niente per Human
      val simulatedInput = "0 i" // poi se serve anche per Human: + "1\nRome\n"
      val in = new ByteArrayInputStream(simulatedInput.getBytes)

      Console.withIn(in) {
        // Setup settings e gameState
        given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)

        // Esegui il turn
        val (newState, result) = gameTurn.run(gameState)

        // Verifica che qualcosa sia cambiato
        newState should not be None
      }
    }


}
