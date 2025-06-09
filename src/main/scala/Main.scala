/**
 * Main entry point of the application.
 *
 * Continuously restarts the game after each playthrough ends.
 * Uses the CLI view to interact with the user.
 */
import StartGame.startGame

object Main:

  /**
   * Main method that starts the application.
   *
   * It enters an infinite loop where the game is played repeatedly.
   * After each game ends, it restarts automatically.
   */
  @main def playGame(): Unit =
    while true do
      startGame()
