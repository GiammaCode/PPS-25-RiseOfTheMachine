package model
import model.util.GameSettings.*
import model.util.GameSettings.Difficulty.*
import model.util.GameSettings.GameMode.*
import model.util.GameSettings.DifficultyConstants.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameDifficultyTest extends AnyFunSuite with Matchers {

  test("AIStats are correctly returned for each difficulty") {
    val easySettings = forSettings(Singleplayer, Easy)
    val normalSettings = forSettings(Singleplayer, Normal)
    val hardSettings = forSettings(Singleplayer, Hard)

    easySettings.ai shouldBe AIStats(infectionPower = easyInfectionPower, sabotagePower = easySabotagePower)
    normalSettings.ai shouldBe AIStats(infectionPower = normalInfectionPower, sabotagePower = normalSabotagePower)
    hardSettings.ai shouldBe AIStats(infectionPower = hardInfectionPower, sabotagePower = hardSabotagePower)
  }

  test("HumanStats are correctly returned for each difficulty") {
    val easySettings = forSettings(Singleplayer, Easy)
    val normalSettings = forSettings(Singleplayer, Normal)
    val hardSettings = forSettings(Singleplayer, Hard)

    easySettings.human shouldBe HumanStats(killSwitch = easyKillSwitch)
    normalSettings.human shouldBe HumanStats(killSwitch = normalKillSwitch)
    hardSettings.human shouldBe HumanStats(killSwitch = hardKillSwitch)
  }

  test("Default AIStats fallback is Normal") {
    val fakeDifficulty = null.asInstanceOf[Difficulty]
    val fallbackSettings = forSettings(Singleplayer, fakeDifficulty)
    fallbackSettings.ai shouldBe AIStats(infectionPower = normalInfectionPower, sabotagePower = normalSabotagePower)
  }

  test("Default HumanStats fallback is Normal") {
    val fakeDifficulty = null.asInstanceOf[Difficulty]
    val fallbackSettings = forSettings(Singleplayer, fakeDifficulty)
    fallbackSettings.human shouldBe HumanStats(killSwitch = normalKillSwitch)
  }
}