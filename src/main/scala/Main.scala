/**
 * Main entry point of the application.
 *
 * Continuously restarts the game after each playthrough ends.
 * Uses the CLI view to interact with the user.
 */
import StartGame.startGame

object Main {
  def main(args: Array[String]): Unit = {
    while (true) {
      StartGame.startGame()
    }
  }
}
