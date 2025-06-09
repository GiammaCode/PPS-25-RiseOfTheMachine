package model

import model.map.WorldMapModule.{createWorldMap, WorldMap}
import model.map.WorldState.*
import model.strategy.{Evolve, Infect, PlayerAI, PlayerHuman, Sabotage}
import model.util.GameSettings.{Difficulty, GameMode, GameSettings, forSettings}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WorldStateFlatSpec extends AnyFlatSpec with Matchers:

  given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Normal)

  val MapSize: Int = 5
  val human: PlayerHuman = PlayerHuman.fromSettings
  val ai: PlayerAI = PlayerAI.fromSettings
  val map: WorldMap = createWorldMap(MapSize)
  val state: WorldState = createWorldState(map, ai, human, 0)

  "WorldState" should "have empty conquered cities initially" in {
    state.AIConqueredCities shouldBe empty
   }

  it should "have attackable cities with non-negative probabilities" in {
    val attackables = state.attackableCities
    attackables should not be empty
    all(attackables.map(_._2)) should be >= 0
    all(attackables.map(_._3)) should be >= 0
  }

  it should "not be in a game-over state initially" in {
    state.isGameOver._1 shouldBe false
  }

  it should "provide correct probability values for actions" in {
    val SecureEvent: Int  = 100
    val (cityName, expectedInfect, expectedSabotage) = state.attackableCities.head
    state.probabilityByCityandAction(cityName, Infect(List(cityName))) shouldEqual expectedInfect
    state.probabilityByCityandAction(cityName, Sabotage(List(cityName))) shouldEqual expectedSabotage
    state.probabilityByCityandAction(cityName, Evolve) shouldEqual SecureEvent
  }
