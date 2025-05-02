import controller.GameController
import model.util.States.State

@main def GameLoop(): Unit =
  var gameState = GameController()
  val maxTurns = 3

  for turn <- 1 to maxTurns do
    println(s"\n--- Turno $turn ---")
    val (newState, _) = gameState.gameTurn().run(gameState)
    gameState = newState

  println("\n--- Fine partita (raggiunto limite massimo di turni) ---")
  println(gameState.worldState.playerAI)

