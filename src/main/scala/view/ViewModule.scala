package view

import model.map.WorldMapModule.WorldMap
import model.map.WorldState.WorldState
import model.strategy.AiAbility.AiAbility
import model.strategy.{PlayerAI, PlayerHuman}
import model.util.GameMode.GameMode


object ViewModule:
  trait GameView:
    def renderGameTurn(worldState: WorldState): (Int, String)
    def renderAiPlayerTurn(worldState: WorldState): (Int, String)
    def renderHumanPlayerTurn(worldState: WorldState): (Int, String)
    def renderGameModeMenu(): GameMode

  object CLIView extends GameView:

    override def renderGameTurn(worldState: WorldState): (Int, String) =
      renderTurn(worldState.turn)
      renderMap(worldState.worldMap)
      renderStatus(worldState.infectionState, worldState.AIUnlockedAbilities)
      renderComplessiveAction(worldState.playerHuman, worldState.playerAI)
      renderProbability(worldState.attackableCities)
      renderActionMenu(worldState.AiOptions)

    override def renderGameModeMenu() : GameMode =
      println("Welcome to Rise of the Machine")
      println("Select game mode:")
      println("1. Single Player")
      println("2. Multiplayer")
      print("Insert your choice > ")

      scala.io.StdIn.readLine().trim match
        case "1" => GameMode.Singleplayer
        case "2" => GameMode.Multiplayer
        case _ =>
          println("Invalid input. Defaulting to Single Player (1).")
          GameMode.Singleplayer

    override def renderAiPlayerTurn(worldState: WorldState): (Int, String) =
      println("\n--- PLAYER AI TURN ---")
      renderMap(worldState.worldMap)
      renderStatus(worldState.infectionState, worldState.AIUnlockedAbilities)
      renderProbability(worldState.attackableCities)
      renderActionMenu(worldState.AiOptions)

    override def renderHumanPlayerTurn(worldState: WorldState): (Int, String) =
      println("\n--- PLAYER HUMAN TURN ---")
      renderComplessiveAction(worldState.playerHuman, worldState.playerAI)
      renderActionMenu(worldState.HumanOptions)



    private def renderTurn(turn: Int): Unit =
      println(s"\n-----RISE OF THE MACHINE - TURN $turn-----\n")

    private def renderMap(worldMap: WorldMap): Unit =
      val mapString = (0 until worldMap.getSizeOfTheMap).map { y =>
        (0 until worldMap.getSizeOfTheMap).map { x =>
          worldMap.findInMap { case (_, coords) => coords.contains((x, y)) }.
            getOrElse("/")
        }.mkString(" ")
      }.mkString("\n")
      println(mapString)

    private def renderStatus(infectionState: (Int, Int), abilities: Set[AiAbility]): Unit =
      val percentageDone = infectionState._1.toDouble / infectionState._2 * 100
      println(s"Infected city: ${infectionState._1}/${infectionState._2} --> $percentageDone%.2f%%".format(percentageDone))
      println(
        Option.when(abilities.nonEmpty)(s"Abilities unlocked: ${abilities.mkString(", ")}")
          .getOrElse("0 abilities unlocked")
      )

    private def renderProbability(cities: Set[(String, Int, Int)]): Unit =
      val formatted = cities.map {
        case (name, infect, sabotage) => s"[$name --> $infect%, $sabotage%]"
      }.mkString("cities probability:  ", " ", "")
      println(formatted)

  def renderActionMenu(options: List[String]): (Int, String) =
    println("Select your action:")
    options.zipWithIndex.foreach { case (option, index) =>
      println(s"$index. $option")
    }
    print("Insert your action > ")

    val input = scala.io.StdIn.readLine().trim.split("\\s+").toList

    input match
      case actionStr :: cityStr :: _ =>
        val actionIndex = actionStr.toIntOption.getOrElse(-1)
        (actionIndex, cityStr)

      case actionStr :: Nil =>
        val actionIndex = actionStr.toIntOption.getOrElse(-1)
        (actionIndex, "")

      case _ =>
        println("Invalid input. Defaulting to (0, \"\")")
        (0, "")

  private def renderComplessiveAction(human: PlayerHuman, ai: PlayerAI) : Unit =
    println(s"Player Human action executed: ${human.executedActions.mkString(", ")}")
    println(s"Player AI action executed: ${ai.executedActions.mkString(", ")}")






