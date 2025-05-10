package model.util

object GameDifficulty:
  enum Difficulty:
    case Easy, Normal, Hard

  final case class AIStats(
                            infectionChance: Int,
                            sabotagePower: Int
                          )

  private val aiConfigs: Map[Difficulty, AIStats] = Map(
    Difficulty.Easy -> AIStats(infectionChance = 70, sabotagePower = 10),
    Difficulty.Normal -> AIStats(infectionChance = 50, sabotagePower = 5),
    Difficulty.Hard -> AIStats(infectionChance = 30, sabotagePower = 3)
  )

  def aiStatsFor(difficulty: Difficulty): AIStats =
    aiConfigs.getOrElse(difficulty, aiConfigs(Difficulty.Normal))
