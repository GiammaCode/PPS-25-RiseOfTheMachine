import controller.GameController
import controller.GameController.{GameState, buildGameState, gameTurn}
import jdk.internal.classfile.impl.AbstractInstruction.UnboundStackInstruction
import model.util.GameSettings.*
import view.ViewModule.CLIView

import scala.annotation.tailrec
object StartGameLoop
def GameLoop(initialState: GameState)(using gameState: GameSettings): Unit =
  val maxTurn= 15
  @tailrec
  def loop(turn: Int, state: GameState)(using gameSettings: GameSettings): GameState =
    if turn > maxTurn || state.isGameOver then state
    else//println(s"\n--- Turno $turn ---")
      val (newState, _) = gameTurn.run(state)
      loop(turn + 1, newState)
  val finalState = loop(1, initialState)
  println("\n--- Fine partita (raggiunto limite massimo di turni) ---")

