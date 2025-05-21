import model.util.GameDifficulty.Difficulty.*
import model.util.GameDifficulty.{AIStats, Difficulty, HumanStats, aiStatsFor, humanStatsFor}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameDifficultyTest extends AnyFunSuite with Matchers{

  test("AIStats are correctly returned for each difficulty") {
    aiStatsFor(Easy) shouldBe AIStats(infectionPower = 70, sabotagePower = 10)
    aiStatsFor(Normal) shouldBe AIStats(infectionPower = 50, sabotagePower = 5)
    aiStatsFor(Hard) shouldBe AIStats(infectionPower = 30, sabotagePower = 3)
  }

  test("HumanStats are correctly returned for each difficulty") {
    humanStatsFor(Easy) shouldBe HumanStats(killSwitch = 0)
    humanStatsFor(Normal) shouldBe HumanStats(killSwitch = 30)
    humanStatsFor(Hard) shouldBe HumanStats(killSwitch = 50)
  }

  test("Default AIStats fallback is Normal") {
    val fakeDifficulty = null.asInstanceOf[Difficulty]
    aiStatsFor(fakeDifficulty) shouldBe AIStats(infectionPower = 50, sabotagePower = 5)
  }

  test("Default HumanStats fallback is Normal") {
    val fakeDifficulty = null.asInstanceOf[Difficulty]
    humanStatsFor(fakeDifficulty) shouldBe HumanStats(killSwitch = 30)
  }

}
