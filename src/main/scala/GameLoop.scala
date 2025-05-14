import controller.GameController
import model.util.GameDifficulty.Difficulty
import model.util.States.State
import view.ViewModule.CLIView

@main def GameLoop(): Unit =
  given Difficulty = Difficulty.Easy //TODO: need to be passed from CLI
  val gameMode = CLIView.renderGameModeMenu()
  var gameState = GameController.apply(gameMode)
  val maxTurns = 3

  for turn <- 1 to maxTurns do

    val (newState, _) = gameState.gameTurn().run(gameState)
    gameState = newState

  println("\n--- Fine partita (raggiunto limite massimo di turni) ---")
  println(gameState.worldState.playerAI)
  println(gameState.worldState.playerHuman)

