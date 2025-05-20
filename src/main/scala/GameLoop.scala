import controller.GameController
import controller.GameController.{GameState, buildGameState, gameTurn}
import model.util.GameDifficulty.Difficulty
import model.util.GameMode.GameMode
import view.ViewModule.CLIView

import scala.annotation.tailrec

@main def GameLoop(): Unit =
  given Difficulty = Difficulty.Easy // TODO: get from CLI

  val initialState = buildGameState()
  val maxTurns = 2


  @tailrec
  def loop(turn: Int, state: GameState): GameState =
    if turn > maxTurns then state
    else
      println(s"\n--- Turno $turn ---")
      val (newState, _) = gameTurn().run(state)
      loop(turn + 1, newState)

  val finalState = loop(1, initialState)
  println("\n--- Fine partita (raggiunto limite massimo di turni) ---")

