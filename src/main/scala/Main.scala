import StartGame.startGame
import controller.GameController.buildGameState
import model.util.GameSettings.GameSettings
import view.ViewModule.CLIView

object Main:
  @main def playGame(): Unit =
    while true do
        startGame()


