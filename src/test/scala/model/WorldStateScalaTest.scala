package model

import model.map.WorldMapModule.{createWorldMap, WorldMap}
import model.map.WorldState.*
import model.strategy.{Evolve, Infect, PlayerAI, PlayerHuman, Sabotage}
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.scalatest.funsuite.AnyFunSuite

class WorldStateScalaTest extends AnyFunSuite:

  given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)

  val human: PlayerHuman = PlayerHuman.fromSettings
  val ai: PlayerAI = PlayerAI.fromSettings
  val map: WorldMap = createWorldMap(5)
  val state: WorldState = createWorldState(map, ai, human, 0)

  test("Conquered cities are initially empty") {
    assert(state.AIConqueredCities.isEmpty)
    assert(state.humanConqueredCities.isEmpty)
  }

  test("Attackable cities are available and valid") {
    val attackables = state.attackableCities
    assert(attackables.nonEmpty)
    assert(attackables.forall { case (_, inf, sab) => inf >= 0 && sab >= 0 })
  }

  test("Initial game is not over") {
    assert(!state.isGameOver._1)
  }

  test("Probability values match for actions") {
    val (cityName, expectedInfect, expectedSabotage) = state.attackableCities.head
    assert(state.probabilityByCityandAction(cityName, Infect(List(cityName))) == expectedInfect)
    assert(state.probabilityByCityandAction(cityName, Sabotage(List(cityName))) == expectedSabotage)
    assert(state.probabilityByCityandAction(cityName, Evolve) == 100)
  }
