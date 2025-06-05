package model.util

import model.strategy.ActionProbabilities

object GameSettings:

  object DifficultyConstants:
    // AI Stats per difficulty
    val EasyInfectionPower = 10
    val EasySabotagePower = 50

    val NormalInfectionPower = 6
    val NormalSabotagePower = 40

    val HardInfectionPower = 3
    val HardSabotagePower = 30

    // Human Stats per difficulty
    val EasyKillSwitch = 0
    val NormalKillSwitch = 5
    val HardKillSwitch = 20

    // Human actions weight
    val EasyModeProbabilities: ActionProbabilities = ActionProbabilities(70, 30, 0)
    val NormalModeProbabilities: ActionProbabilities = ActionProbabilities(33, 33, 34)
    val HardModeProbabilities: ActionProbabilities = ActionProbabilities(40, 100, 100)

  enum Difficulty:
    case Easy, Normal, Hard

  enum GameMode:
    case Singleplayer, Multiplayer

  /**
   * Configuration for AI-controlled player behavior.
   *
   * @param infectionPower Chance (as percentage) that the AI can infect a city.
   * @param sabotagePower   Power level of the AI's sabotage action.
   */
  final case class AIStats(
                            infectionPower: Int,
                            sabotagePower: Int
                          )

  /**
   * Configuration for human-controlled player behavior.
   *
   * @param killSwitch Represents the effectiveness or cooldown of the kill switch feature.
   */
  final case class HumanStats(
                               killSwitch: Int,
                               actionsWeight: ActionProbabilities
                             )

  /**
   * Aggregates all game configuration settings.
   *
   * @param gameMode   Selected game mode.
   * @param difficulty Selected difficulty level.
   * @param ai         AI player stats based on difficulty.
   * @param human      Human player stats based on difficulty.
   */
  final case class GameSettings(
                                 gameMode: GameMode,
                                 difficulty: Difficulty,
                                 ai: AIStats,
                                 human: HumanStats
                               )

  import Difficulty._
  import GameMode._
  import DifficultyConstants._

  private val aiConfigs: Map[Difficulty, AIStats] = Map(
    Easy -> AIStats(infectionPower = EasyInfectionPower, sabotagePower = EasySabotagePower),
    Normal -> AIStats(infectionPower = NormalInfectionPower, sabotagePower = NormalSabotagePower),
    Hard -> AIStats(infectionPower = HardInfectionPower, sabotagePower = HardSabotagePower)
  )

  private val humanConfigs: Map[Difficulty, HumanStats] = Map(
    Easy -> HumanStats(killSwitch = EasyKillSwitch, actionsWeight = EasyModeProbabilities),
    Normal -> HumanStats(killSwitch = NormalKillSwitch, actionsWeight = NormalModeProbabilities),
    Hard -> HumanStats(killSwitch = HardKillSwitch, actionsWeight = HardModeProbabilities)
  )

  /**
   * Factory method to generate a full GameSettings instance based on mode and difficulty.
   *
   * @param gameMode   The selected game mode.
   * @param difficulty The selected difficulty level.
   * @return A complete GameSettings object containing derived AI and human stats.
   */
  def forSettings(gameMode: GameMode, difficulty: Difficulty): GameSettings =
    val ai = aiConfigs.getOrElse(difficulty, aiConfigs(Normal))
    val human = humanConfigs.getOrElse(difficulty, humanConfigs(Normal))
    GameSettings(gameMode, difficulty, ai, human)

  /**
   * Given instance to derive GameSettings if GameMode and Difficulty are available in context.
   */
  given gameSettings(using gameMode: GameMode, difficulty: Difficulty): GameSettings =
  forSettings(gameMode, difficulty)

  /**
   * Given instance to extract Difficulty from GameSettings.
   */
  given difficulty(using settings: GameSettings): Difficulty = settings.difficulty

  /**
   * Given instance to extract GameMode from GameSettings.
   */
  given gameMode(using settings: GameSettings): GameMode = settings.gameMode

  /**
   * Given instance to extract AIStats from GameSettings.
   */
  given aiStats(using settings: GameSettings): AIStats = settings.ai

  /**
   * Given instance to extract HumanStats from GameSettings.
   */
  given humanStats(using settings: GameSettings): HumanStats = settings.human