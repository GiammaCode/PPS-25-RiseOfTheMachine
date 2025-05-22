import controller.GameController
import controller.GameController.{GameState, gameTurn}
import model.util.GameSettings.*

import scala.annotation.tailrec
object StartGameLoop
def GameLoop(initialState: GameState)(using gameState: GameSettings): Unit =
  @tailrec
  def loop(state: GameState)(using gameSettings: GameSettings): GameState =
    if state.isGameOver then state
    else
      val (newState, _) = gameTurn.run(state)
      loop(newState)
  val finalState = loop(initialState)

