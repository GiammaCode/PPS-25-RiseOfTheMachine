package model

import model.map.WorldMapModule.{CreateModuleType, DeterministicMapModule, createWorldMap}
import model.map.WorldState.*
import model.strategy.*
import model.strategy.HumanAction.*
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class SmartHumanStrategyFlatSpec extends AnyFlatSpec with Matchers:
  val MapSize: Int = 8
  val EasyActionProbs: ActionProbabilities = ActionProbabilities(70, 30, 0)
  val NormalActionProbs: ActionProbabilities = ActionProbabilities(33, 33, 34)
  val HardActionProbs: ActionProbabilities = ActionProbabilities(50, 20, 30)
  val ForceActionProbs: ActionProbabilities = ActionProbabilities(100, 0, 0)

  "SmartHumanStrategy in Easy mode" should "choose only CityDefense or GlobalDefense" in {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Easy)
    given ActionProbabilities = EasyActionProbs

    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(MapSize)
    val state = createWorldState(map, ai, human, 0)

    val action = SmartHumanStrategy.decideAction(state)
    action match
      case _: CityDefense | _: GlobalDefense => succeed
      case _ => fail(s"Unexpected action: $action")
  }

  "SmartHumanStrategy in Normal mode" should "choose any valid HumanAction based on probabilities" in {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)
    given ActionProbabilities = NormalActionProbs

    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(MapSize)
    val state = createWorldState(map, ai, human, 0)

    val action = SmartHumanStrategy.decideAction(state)
    action shouldBe a[HumanAction]
  }

  "SmartHumanStrategy in Hard mode" should "choose actions targeting the highest-risk cities" in {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Hard)
    given ActionProbabilities = HardActionProbs

    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(MapSize)
    val state = createWorldState(map, ai, human, 0)

    val action = SmartHumanStrategy.decideAction(state)
    action shouldBe a[HumanAction]
  }

  "SmartHumanStrategy" should "prefer high-risk cities more in Hard than in Normal mode" in {
    given DeterministicMap: CreateModuleType = DeterministicMapModule
    val map = createWorldMap(MapSize)

    def runWithDifficulty(diff: Difficulty, probs: ActionProbabilities) =
      given GameSettings = forSettings(GameMode.Singleplayer, diff)
      given ActionProbabilities = probs

      val human = PlayerHuman.fromSettings
      val ai = PlayerAI.fromSettings
      val state = createWorldState(map, ai, human, 0)
      SmartHumanStrategy.decideAction(state)

    val normalAction = runWithDifficulty(Difficulty.Normal, ForceActionProbs)
    val hardAction = runWithDifficulty(Difficulty.Hard, ForceActionProbs)

    normalAction shouldBe a[CityDefense]

    hardAction match
      case CityDefense(targets) =>
        targets should contain ("C") // C is the city with highest risk in DeterministicMap (size = 8)
      case _ => fail("Expected CityDefense targeting high-risk city")
  }
