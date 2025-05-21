package model.util

object GameSettings:


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
                               killSwitch: Int
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

  private val aiConfigs: Map[Difficulty, AIStats] = Map(
    Easy -> AIStats(infectionPower = 10, sabotagePower = 50),
    Normal -> AIStats(infectionPower = 6, sabotagePower = 40),
    Hard -> AIStats(infectionPower = 3, sabotagePower = 30)
  )

  private val humanConfigs: Map[Difficulty, HumanStats] = Map(
    Easy -> HumanStats(killSwitch = 0),
    Normal -> HumanStats(killSwitch = 30),
    Hard -> HumanStats(killSwitch = 50)
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
