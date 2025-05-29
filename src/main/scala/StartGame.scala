import controller.GameController.GameState
import model.util.GameSettings.*
import view.ViewModule.CLIView

import scala.annotation.tailrec
object StartGame:
  import GameSettings.given
  def startGame(): Unit =

    given GameSettings = CLIView.renderGameModeMenu()
    import controller.GameController

    val initialState = GameController.buildGameState

    @tailrec
    def gameLoop(state: GameState): GameState =
      if state.isGameOver then state
      else
        val (newState, _) = GameController.gameTurn.run(state)
        gameLoop(newState)
    gameLoop(initialState)

