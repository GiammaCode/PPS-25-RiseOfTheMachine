package controller

import model.map.WorldMapModule.WorldMap
import model.strategy.*

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  def apply(): GameState =
    val (ai, human, worldMap) = model.GameFactory.createGame()
      GameState(ai, human, worldMap)

case class GameState(ai: PlayerAI,
                     human: PlayerHuman,
                     worldMap: WorldMap):

  private var currentAi : PlayerAI = ai
  private var currentHuman : PlayerHuman = human
  private var currentMap : WorldMap = worldMap



  import model.util.States.State.State

  def playerAction(action: AiAction): State[GameState, Unit] =
    State { gs => ???}


  def humanAction(action: HumanAction): State[GameState, Unit] =
    State { gs => ???}

 /*   def gameTurn(turn: Int): State[GameState, Unit] = for
      action = rendermapAndwaitForPlayer(worldMap)
      _ <- playerAction(action)
      _ <- humanAction(action)
    yield ()*/

  def startGame() : Unit = ???
  /**
   * Execute a full turn
   * @return the updated controller
   */
  def executeTurn(): Unit = ???


  def rendermapAndwaitForPlayer(worldMap: WorldMap): TurnAction = ???


