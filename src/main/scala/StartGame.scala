/**
 * Entry point for starting the game.
 *
 * This object handles the initial setup and manages the main game loop.
 */
import controller.GameController.GameState
import model.util.GameSettings.*
import view.ViewModule.CLIView

import scala.annotation.tailrec

object StartGame:

  /**
   * Starts the game by:
   *  - Rendering the game mode menu to configure settings
   *  - Initializing the game state
   *  - Entering the recursive game loop
   */
  def startGame(): Unit =

    given GameSettings = CLIView.renderGameModeMenu()
    import model.map.WorldMapModule.given

    import controller.GameController

    val initialState = GameController.buildGameState

    /**
     * The main game loop, which processes turns recursively until the game is over.
     *
     * @param state The current state of the game
     * @return The final state when the game is over
     */
    @tailrec
    def gameLoop(state: GameState): GameState = state match
      case s if s.isGameOver => s
      case s =>
        val (newState, _) = GameController.gameTurn.run(s)
        gameLoop(newState)

    gameLoop(initialState)
