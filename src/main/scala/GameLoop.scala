import controller.GameController
import controller.GameController.initialGameState
import model.util.States.State

@main def GameLoop(): Unit =
  var gameState = initialGameState
  val maxTurns = 5

  for turn <- 1 to maxTurns do
    println(s"\n--- Turno $turn ---")
    val (newState, _) = gameState.gameTurn().run(gameState)
    gameState = newState

  println("\n--- Fine partita (raggiunto limite massimo di turni) ---")
  println(gameState.worldState.playerAI)

