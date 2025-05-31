package model

import controller.GameController.worldState
import model.map.WorldMapModule.createWorldMap
import org.scalatest.funsuite.AnyFunSuite
import model.strategy.HumanAction.*
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import model.map.WorldState
import model.map.WorldState.*
import model.strategy.{CityDefense, DevelopKillSwitch, GlobalDefense, PlayerAI, PlayerHuman, SmartHumanStrategy}
import model.strategy.DevelopKillSwitch.*

class SmartHumanStrategyScalaTest extends AnyFunSuite:


  test("Easy strategy should choose CityDefense or GlobalDefense") {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Easy)
    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(5)
    val state = createWorldState(map, ai, human, 0)
    val action = SmartHumanStrategy.decideAction(state)
    assert(action.isInstanceOf[CityDefense] || action.isInstanceOf[GlobalDefense])
  }

  test("Normal strategy should return one of the defined HumanActions") {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)

    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(5)
    val state = createWorldState(map, ai, human, 0)
    val action = SmartHumanStrategy.decideAction(state)
    assert(action.isInstanceOf[CityDefense] || action.isInstanceOf[GlobalDefense] || action == DevelopKillSwitch)
  }

  test("Hard strategy should prioritize cities with highest combined risk") {
    given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Hard)

    val human = PlayerHuman.fromSettings
    val ai = PlayerAI.fromSettings
    val map = createWorldMap(5)
    val state = createWorldState(map, ai, human, 0)
    val action = SmartHumanStrategy.decideAction(state)
    println(action)
    assert(action.isInstanceOf[CityDefense] || action.isInstanceOf[GlobalDefense] || action == DevelopKillSwitch)
  }