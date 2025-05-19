package model.util

object GameSettings:

  enum Difficulty:
    case Easy, Normal, Hard

  enum GameMode:
    case Singleplayer, Multiplayer

  final case class AIStats(
                            infectionChance: Int,
                            sabotagePower: Int
                          )

  final case class HumanStats(
                               killSwitch: Int
                             )

  final case class GameSettings(
                                 gameMode: GameMode,
                                 difficulty: Difficulty,
                                 ai: AIStats,
                                 human: HumanStats
                               )

  import Difficulty._
  import GameMode._

  // Configuration maps indexed by Difficulty
  private val aiConfigs: Map[Difficulty, AIStats] = Map(
    Easy -> AIStats(infectionChance = 70, sabotagePower = 10),
    Normal -> AIStats(infectionChance = 50, sabotagePower = 5),
    Hard -> AIStats(infectionChance = 30, sabotagePower = 3)
  )

  private val humanConfigs: Map[Difficulty, HumanStats] = Map(
    Easy -> HumanStats(killSwitch = 0),
    Normal -> HumanStats(killSwitch = 30),
    Hard -> HumanStats(killSwitch = 50)
  )

  /** Factory method to build GameSettings from mode and difficulty */
  def forSettings(gameMode: GameMode, difficulty: Difficulty): GameSettings =
    val ai = aiConfigs.getOrElse(difficulty, aiConfigs(Normal))
    val human = humanConfigs.getOrElse(difficulty, humanConfigs(Normal))
    GameSettings(gameMode, difficulty, ai, human)

  // Givens to construct GameSettings given GameMode and Difficulty
  given gameSettings(using gameMode: GameMode, difficulty: Difficulty): GameSettings =
  forSettings(gameMode, difficulty)

  // Extract Difficulty and GameMode from GameSettings
  given difficulty(using settings: GameSettings): Difficulty = settings.difficulty
  given gameMode(using settings: GameSettings): GameMode = settings.gameMode

  // Provide AIStats and HumanStats from GameSettings
  given aiStats(using settings: GameSettings): AIStats = settings.ai
  given humanStats(using settings: GameSettings): HumanStats = settings.human
