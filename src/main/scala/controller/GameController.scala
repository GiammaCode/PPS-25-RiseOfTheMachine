package controller

import model.map.WorldMapModule.WorldMap
import model.strategy.*
import model.strategy.PlayerAI.PlayerAI
import view.ViewModule.*

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  def apply(): GameState =
    val (ai, human, worldMap) = model.GameFactory.createGame()
    val view = CLIView
    GameState(ai, human, worldMap,view)

case class GameState(ai: PlayerAI,
                     human: PlayerHuman,
                     worldMap: WorldMap,
                      view: GameView):

  private val currentAi : PlayerAI = ai
  private val currentHuman : PlayerHuman = human
  private var currentMap : WorldMap = worldMap


  import model.util.States.State.State

  private def doPlayerAction(action: AiAction): State[GameState, Unit] =
    State { gs =>
      (gs.copy(ai = gs.currentAi.executeAction(action)), ())}

  private def doHumanAction(action: HumanAction): State[GameState, Unit] =
    State { gs => (gs.copy(human = gs.currentHuman.executeAction(action)),())}

  def gameTurn(aiAction: AiAction, humanAction: HumanAction): State[GameState, Unit] =
      for
        _ <- doPlayerAction(aiAction)
        _ <- doHumanAction(humanAction)
      yield()

  def startGame() : Unit = ???



