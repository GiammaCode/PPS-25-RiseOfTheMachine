package view

import model.map.WorldMapModule.WorldMap
import model.map.WorldState.WorldState
import model.strategy.AiAbility.AiAbility
import model.strategy.{PlayerAI, PlayerHuman}
import model.util.GameMode.GameMode
import model.util.GameDifficulty.Difficulty

import scala.io.StdIn


object ViewModule:
  trait GameView:
    def renderGameModeMenu(): (GameMode, Difficulty)
    def renderGameTurn(worldState: WorldState)(using GameMode): ((Int, String), Option[(Int, String)])

  object CLIView extends GameView:
    override def renderGameModeMenu(): (GameMode, Difficulty) =
      println(
        """|🎮 Welcome to Rise of the Machine
           |──────────────────────────────────────────
           |  💥 Select game mode:
           |  1. Single Player
           |  2. Multiplayer
           |──────────────────────────────────────────
           |Insert your choice >""".stripMargin
      )
      val selectedMode: GameMode = StdIn.readLine().trim match
        case "1" => GameMode.Singleplayer
        case "2" => GameMode.Multiplayer
        case _ =>
          println("Invalid input. Defaulting to Single Player (1).")
          GameMode.Singleplayer

      val selectedDifficulty: Difficulty = selectedMode match
        case GameMode.Singleplayer =>
          println(
            """|──────────────────────────────────────────
               |  📊 Select Difficulty Level
               |  1. Easy
               |  2. Normal
               |  3. Hard
               |──────────────────────────────────────────
               |Insert your choice >""".stripMargin
          )
          StdIn.readLine().trim match
            case "1" => Difficulty.Easy
            case "2" => Difficulty.Normal
            case "3" => Difficulty.Hard
            case _ =>
              println("Invalid input.Defaulting to Normal difficulty")
              Difficulty.Normal

        case GameMode.Multiplayer => Difficulty.Normal

      (selectedMode, selectedDifficulty)

    override def renderGameTurn(worldState: WorldState)(using gameMode: GameMode): ((Int, String), Option[(Int, String)]) =
      renderTurn(worldState.turn)
      renderMap(worldState.worldMap)
      renderStatus(worldState.infectionState, worldState.AIUnlockedAbilities)
      renderProbability(worldState.attackableCities)
      renderComplessiveAction(worldState.playerHuman, worldState.playerAI)
      gameMode match
        case GameMode.Singleplayer =>
          println("\n AI PLAYER TURN")
          val aiMove = renderActionMenu(worldState.AiOptions)
          ((aiMove), None)

        case GameMode.Multiplayer =>
          println("\n AI PLAYER TURN")
          val aiMove = renderActionMenu(worldState.AiOptions)
          println("\n HUMAN PLAYER TURN")
          val humanMove = renderActionMenu(worldState.HumanOptions)
          ((aiMove), Some(humanMove))


    private def renderTurn(turn: Int): Unit =
      println(s"\n🌍 ----- RISE OF THE MACHINE - TURN $turn -----\n")

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
    println("╭──────────────────────────────╮")
    println("│       Select your action     │")
    options.zipWithIndex.foreach { case (option, index) =>
      println(f"│ $index%2d. $option%-20s     │")
    }
    println("╰──────────────────────────────╯")
    print("Insert your action > ")

    val input = StdIn.readLine().trim.split("\\s+").toList

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

  private def renderComplessiveAction(human: PlayerHuman, ai: PlayerAI): Unit =
    println("\n🧾 Action Summary")
    println(s"🧍 Human: ${human.executedActions.mkString(" || ")}")
    println(s"🤖 AI   : ${ai.executedActions.lastOption.mkString(" || ")}")






