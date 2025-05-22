import controller.GameController.GameState
import model.util.GameSettings.*

import scala.annotation.tailrec
object StartGame:
  def startGame(using gameState: GameSettings): Unit =

    import controller.GameController

    val initialState = GameController.buildGameState

    @tailrec
    def gameLoop(state: GameState)(using gameSettings: GameSettings): GameState =
      if state.isGameOver then state
      else
        val (newState, _) = GameController.gameTurn.run(state)
        gameLoop(newState)

    gameLoop(initialState)

