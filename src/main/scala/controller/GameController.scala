package controller

import model.strategy.{AiAction, HumanAction, PlayerAI, PlayerEntity, PlayerHuman}
import model.map.WorldMapModule.WorldMap
import model.map.WorldStateModule
import view.{CLIView, GameView}

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  def apply(): GameController =
    val (ai, human, worldMap) = model.GameFactory.createGame()
    new GameController(ai, human, worldMap, CLIView())

case class GameController(
                           ai: PlayerAI,
                           human: PlayerHuman,
                           worldMap: WorldMap,
                           view: GameView,
                         ):

  private var currentAi : PlayerAI = ai
  private var currentHuman : PlayerHuman = human
  private var currentMap : WorldMap = worldMap

  def startGame() : Unit = ???
  /**
   * Execute a full turn
   * @return the updated controller
   */
  def executeTurn(): Unit = ???



