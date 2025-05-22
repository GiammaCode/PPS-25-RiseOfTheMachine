import controller.GameController.buildGameState

object Main:
  @main def playGame(): Unit =
    while true do
      val initialState = buildGameState()
      val maxTurn = 15
      GameLoop(initialState, 15)


