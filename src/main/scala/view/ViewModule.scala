package view

import model.map.WorldMapModule.WorldMap
import model.map.WorldState.WorldState
import model.strategy.AiAbility.AiAbility
import model.strategy.{PlayerAI, PlayerHuman}
import model.util.GameSettings.{GameMode, GameSettings}

import scala.io.StdIn


object ViewModule:
  trait GameView:
    def renderGameModeMenu(): GameMode

    def renderGameTurn(worldState: WorldState)(gameMode:GameMode): ((Int, String), Option[(Int, String)])
  //def renderAiPlayerTurn(worldState: WorldState): (Int, String)
  //def renderHumanPlayerTurn(worldState: WorldState): (Int, String)

  object CLIView extends GameView:
    override def renderGameModeMenu(): GameMode =
      println(
        """|Welcome to Rise of the Machine
           |Select game mode:
           |1. Single Player
           |2. Multiplayer
           |Insert your choice >""".stripMargin
      )
      val input = StdIn.readLine().trim

      Option(input).flatMap {
        case "1" => Some(GameMode.Singleplayer)
        case "2" => Some(GameMode.Multiplayer)
        case _ => None
      }.getOrElse {
        println("Invalid input. Defaulting to Single Player (1).")
        GameMode.Singleplayer
      }

    override def renderGameTurn(worldState: WorldState)(gameMode: GameMode): ((Int, String), Option[(Int, String)]) =
      renderTurn(worldState.turn)
      renderMap(worldState.worldMap)
      renderStatus(worldState.infectionState, worldState.AIUnlockedAbilities)
      renderComplessiveAction(worldState.playerHuman, worldState.playerAI)
      renderProbability(worldState.attackableCities)
      gameMode match
        case GameMode.Singleplayer =>
          val aiMove = renderActionMenu(worldState.AiOptions)
          ((aiMove), None)

        case GameMode.Multiplayer =>
          val aiMove = renderActionMenu(worldState.AiOptions)
          val humanMove = renderActionMenu(worldState.HumanOptions)
          ((aiMove), Some(humanMove))


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
    println(s"Player Human action executed: ${human.executedActions.mkString(", ")}")
    println(s"Player AI action executed: ${ai.executedActions.mkString(", ")}")






