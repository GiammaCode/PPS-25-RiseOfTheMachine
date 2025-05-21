package model.util

object GameDifficulty:
  enum Difficulty:
    case Easy, Normal, Hard

  final case class AIStats(
                            infectionPower: Int,
                            sabotagePower: Int
                          )

  private val aiConfigs: Map[Difficulty, AIStats] = Map(
    Difficulty.Easy -> AIStats(infectionPower = 10, sabotagePower = 50),
    Difficulty.Normal -> AIStats(infectionPower = 5, sabotagePower = 40),
    Difficulty.Hard -> AIStats(infectionPower = 3, sabotagePower = 30)
  )

  def aiStatsFor(difficulty: Difficulty): AIStats =
    aiConfigs.getOrElse(difficulty, aiConfigs(Difficulty.Normal))

  

  final case class HumanStats(killSwitch: Int)

  private val humanConfigs: Map[Difficulty, HumanStats] = Map(
    Difficulty.Easy -> HumanStats(killSwitch = 0),
    Difficulty.Normal -> HumanStats(killSwitch = 30),
    Difficulty.Hard -> HumanStats(killSwitch = 50)
  )

  def humanStatsFor(difficulty: Difficulty): HumanStats =
    humanConfigs.getOrElse(difficulty, humanConfigs(Difficulty.Normal))

  /**Given section*/
  given aiStats(using d: Difficulty): AIStats = aiStatsFor(d)
  given humanStats(using d: Difficulty) : HumanStats = humanStatsFor(d)