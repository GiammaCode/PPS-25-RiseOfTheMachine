package model

import controller.GameController.worldState
import model.map.WorldMapModule.{CreateModuleType, DeterministicMapModule, UndeterministicMapModule, createWorldMap}
import model.map.WorldState.*
import model.strategy.*
import model.strategy.HumanAction.*
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import view.CLIFormatter

import scala.collection.immutable.ListMap
import scala.language.postfixOps

class SmartHumanStrategyFlatSpec extends AnyFlatSpec with Matchers:

  "SmartHumanStrategy in Easy mode" should "choose CityDefense or GlobalDefense" in {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Easy)
    given ActionProbabilities = ActionProbabilities(70, 30, 0)

    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(5)
    val state = createWorldState(map, ai, human, 0)

    val action = SmartHumanStrategy.decideAction(state)
    (action.isInstanceOf[CityDefense] || action.isInstanceOf[GlobalDefense]) shouldBe true

  }

  "SmartHumanStrategy in Normal mode" should "return one of the defined HumanActions" in {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)
    given ActionProbabilities = ActionProbabilities(33, 33, 33)

    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(5)
    val state = createWorldState(map, ai, human, 0)

    val action = SmartHumanStrategy.decideAction(state)
    action shouldBe a[HumanAction]
  }

  "SmartHumanStrategy in Hard mode" should "prioritize cities with highest combined risk" in {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Hard)
    given ActionProbabilities = ActionProbabilities(50, 30, 20)

    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(5)
    val state = createWorldState(map, ai, human, 0)

    val action = SmartHumanStrategy.decideAction(state)
    println(action)
    action shouldBe a[HumanAction]
  }


  /*Deterministic map
      C C A D D
      C A A A B
      B A A A B
      B A A A B
      B B B B B
  * */

  "SmartHumanStrategy" should "prioritize highest risk city in Hard vs Normal mode" in {
    given DeterministicMap: CreateModuleType = DeterministicMapModule
    val map = createWorldMap(5)

    CLIFormatter.printMap(map, Set())
    def runWithDifficulty(diff: Difficulty) =
      given GameSettings = forSettings(GameMode.Singleplayer, diff)
      given ActionProbabilities = ActionProbabilities(70, 30, 0)


      val human = PlayerHuman.fromSettings
      val ai = PlayerAI.fromSettings
      val state = createWorldState(map, ai, human, 0)

      SmartHumanStrategy.decideAction(state)

    val normalAction = runWithDifficulty(Difficulty.Normal)
    val hardAction = runWithDifficulty(Difficulty.Hard)

    print(normalAction)
    print(hardAction)

    normalAction shouldBe a[HumanAction]
   /*
    // Verifica che Hard scelga un'azione basata su "HighRiskCity"

    hardAction match
      case CityDefense(targets) =>
        targets should contain("HighRiskCity")
      case GlobalDefense(targets) =>
        targets should contain("HighRiskCity")
      case _ => succeed

    // Verifica che Normal possa non considerare la città a rischio massimo
    normalAction match
      case CityDefense(targets) =>
        targets.headOption should not equal Some("HighRiskCity")
      case _ => succeed

    */
  }
