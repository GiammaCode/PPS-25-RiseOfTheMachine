import controller.GameController
import controller.GameController.buildGameState
import model.util.GameDifficulty.Difficulty

@main def GameLoop(): Unit =
  given Difficulty = Difficulty.Easy //TODO: need to be passed from CLI
  var gameState = buildGameState
  val maxTurns = 2

  for turn <- 1 to maxTurns do
    println(s"\n--- Turno $turn ---")
    val (newState, _) = gameState.gameTurn().run(gameState)
    gameState = newState

  println("\n--- Fine partita (raggiunto limite massimo di turni) ---")
  println(gameState.worldState.playerAI)

