package model
import model.util.GameSettings.*
import model.util.GameSettings.Difficulty.*
import model.util.GameSettings.GameMode.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameDifficultyTest extends AnyFunSuite with Matchers {

  test("AIStats are correctly returned for each difficulty") {
    val easySettings = forSettings(Singleplayer, Easy)
    val normalSettings = forSettings(Singleplayer, Normal)
    val hardSettings = forSettings(Singleplayer, Hard)

    easySettings.ai shouldBe AIStats(infectionChance = 70, sabotagePower = 10)
    normalSettings.ai shouldBe AIStats(infectionChance = 50, sabotagePower = 5)
    hardSettings.ai shouldBe AIStats(infectionChance = 30, sabotagePower = 3)
  }

  test("HumanStats are correctly returned for each difficulty") {
    val easySettings = forSettings(Singleplayer, Easy)
    val normalSettings = forSettings(Singleplayer, Normal)
    val hardSettings = forSettings(Singleplayer, Hard)

    easySettings.human shouldBe HumanStats(killSwitch = 0)
    normalSettings.human shouldBe HumanStats(killSwitch = 30)
    hardSettings.human shouldBe HumanStats(killSwitch = 50)
  }

  test("Default AIStats fallback is Normal") {
    val fakeDifficulty = null.asInstanceOf[Difficulty]
    val fallbackSettings = forSettings(Singleplayer, fakeDifficulty)
    fallbackSettings.ai shouldBe AIStats(infectionChance = 50, sabotagePower = 5)
  }

  test("Default HumanStats fallback is Normal") {
    val fakeDifficulty = null.asInstanceOf[Difficulty]
    val fallbackSettings = forSettings(Singleplayer, fakeDifficulty)
    fallbackSettings.human shouldBe HumanStats(killSwitch = 30)
  }
}
