package view

import model.map.WorldMapModule.WorldMap
import model.map.WorldState.WorldState
import model.strategy.AiAbility.AiAbility
import model.strategy.{ActionTarget, AiAction, CityDefense, DevelopKillSwitch, Evolve, GlobalDefense, Infect, PlayerAI, PlayerHuman, Sabotage, TurnAction}
import model.util.GameSettings.*

import scala.io.StdIn

/**
 * ViewModule contains the trait for abstracting the game UI,
 * and a concrete implementation for command-line interaction (CLIView).
 */
object ViewModule:

  /**
   * Trait representing the view layer of the game.
   * It abstracts rendering menus, turns, and actions based on the current WorldState.
   */
  trait GameView:
    /**
     * Renders the main menu and asks the player to select the game mode.
     * If Single Player is selected, it also asks for the difficulty level.
     *
     * @return a tuple containing the selected GameMode and Difficulty.
     */
    def renderGameModeMenu(): GameSettings

    /**
     * Renders the current turn, showing the map, infection state, unlocked abilities,
     * and asks for player input depending on the GameMode.
     *
     * @param worldState the current game state to render.
     * @param gameMode   the game mode (Singleplayer or Multiplayer), passed implicitly.
     * @return a tuple containing the AI player's action and optionally the human player's action.
     */
    def renderGameTurn(worldState: WorldState)(using GameSettings): ((Int, String), Option[(Int, String)])

  /**
   * CLIView is the command-line implementation of GameView.
   * It renders all game content to the terminal and handles user input via StdIn.
   */
  object CLIView extends GameView:

    /**
     * Prompts the player to choose the game mode, then (if Singleplayer)
     * also requests the difficulty level.
     *
     * @return a tuple containing the chosen GameMode and Difficulty
     */
    override def renderGameModeMenu(): GameSettings =
      println("╭──────────────────────────────╮")
      println("│  🎮 Welcome to RotMa         │")
      println("│  📊 Select Difficulty Level  │")
      println("│  1. Single Player            │")
      println("│  2. Multiplayer              │")
      println("╰──────────────────────────────╯")
      print("Insert your choice > ")
      val selectedMode: GameMode = StdIn.readLine().trim match
        case "1" => GameMode.Singleplayer
        case "2" => GameMode.Multiplayer
        case _ =>
          println("Invalid input. Defaulting to Single Player.")
          GameMode.Singleplayer

      val selectedDifficulty: Difficulty = selectedMode match
        case GameMode.Singleplayer =>
          println("╭──────────────────────────────╮")
          println("│  📊 Select Difficulty Level  │")
          println("│  1. Easy                     │")
          println("│  2. Normal                   │")
          println("│  3. Hard                     │")
          println("╰──────────────────────────────╯")
          print("Insert your choice > ")
          StdIn.readLine().trim match
            case "1" => Difficulty.Easy
            case "2" => Difficulty.Normal
            case "3" => Difficulty.Hard
            case _ =>
              println("Invalid input. Defaulting to Normal difficulty")
              Difficulty.Normal

        case GameMode.Multiplayer => Difficulty.Normal

      forSettings(selectedMode, selectedDifficulty)

    /**
     * Renders the current game turn, including map, infection status,
     * unlocked AI abilities, and the list of previous actions.
     *
     * Then prompts the player (AI and optionally Human) to choose an action. -
     *
     * @param worldState the current world state
     * @param gameMode   the selected game mode (implicit)
     * @return a tuple: (AIPlayer input, Optional HumanPlayer input)
     */
    override def renderGameTurn(worldState: WorldState)(using gameSettings: GameSettings): ((Int, String), Option[(Int, String)]) =
      renderTurn(worldState.turn)
      renderMap(worldState.worldMap)
      renderStatus(worldState.infectionState, worldState.AIUnlockedAbilities)
      renderProbability(worldState.attackableCities)
      renderComplessiveAction(worldState.playerHuman, worldState.playerAI)
      gameSettings.gameMode match
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

    /**
     * Prints the current turn number in a stylized header.
     *
     * @param turn the current turn index
     */
    private def renderTurn(turn: Int): Unit =
      println(s"\n🌍 --- RISE OF THE MACHINE - TURN $turn ---\n")

    /**
     * Renders the current state of the world map to the terminal.
     *
     * @param worldMap the current WorldMap
     */
    private def renderMap(worldMap: WorldMap): Unit =
      println("       \uD83D\uDDFA\uFE0F  World Map")
      val mapString = (0 until worldMap.getSizeOfTheMap).map { y =>
        (0 until worldMap.getSizeOfTheMap).map { x =>
          worldMap.findInMap { case (_, coords) => coords.contains((x, y)) }.
            getOrElse("/")
        }.mkString(" ")
      }.mkString("\n    ")
      println("    " + mapString)

    /**
     * Displays the infection progress and unlocked abilities of the AI.
     *
     * @param infectionState tuple of (infected cities, total cities)
     * @param abilities      the currently unlocked AiAbilities
     */
    private def renderStatus(infectionState: (Int, Int), abilities: Set[AiAbility]): Unit =
      val percentageDone = infectionState._1.toDouble / infectionState._2 * 100
      println(s"Infected city: ${infectionState._1}/${infectionState._2} --> $percentageDone%.2f%%".format(percentageDone))
      println(
        Option.when(abilities.nonEmpty)(s"Abilities unlocked: ${abilities.mkString(", ")}")
          .getOrElse("0 abilities unlocked")
      )

    /**
     * Renders the infection and sabotage probabilities for each attackable city.
     *
     * @param cities a set of tuples (cityName, infectionChance, sabotageChance)
     */
    private def renderProbability(cities: Set[(String, Int, Int)]): Unit =
      if cities.nonEmpty then
        val formatted = cities.toSeq
          .sortBy(_._1) // opzionale: ordina alfabeticamente per leggibilità
          .map { case (name, infect, sabotage) =>
            f"- 📍 $name%-3s | 🦠Infect: $infect%3d%% | 🧨Sabotage: $sabotage%3d%%"
          }
          .mkString("\n")
        println("Cities probability:\n" + formatted)
      else
        println("No attackable cities.")

    /**
     * Displays the executed actions of both Human and AI players.
     *
     * @param human the human player
     * @param ai    the AI player
     */
    private def renderComplessiveAction(human: PlayerHuman, ai: PlayerAI): Unit =
      def formatAction(action: TurnAction): String = action match
        case Infect(targets) => s"Infect(${targets.mkString(", ")})"
        case Sabotage(targets) => s"Sabotage(${targets.mkString(", ")})"
        case Evolve => "Evolve"
        case DevelopKillSwitch => "DevelopKillSwitch"
        case CityDefense(targets) => s"CityDefense(${targets.mkString(", ")})"
        case GlobalDefense(targets) => s"GlobalDefense"

      println("\n🧾 Action Summary")
      println(s"🧍 Human: ${human.executedActions.map(formatAction).mkString(" || ")}")
      println(s"🤖 AI   : ${ai.executedActions.map(formatAction).mkString(" || ")}")


    /**
     * Renders a stylized menu of available actions to the terminal.
     * Allows the user to input an action index and optionally a city name.
     *
     * @param options the list of action names to display
     * @return a tuple (actionIndex, targetCityName)
     */
    private def renderActionMenu(options: List[String]): (Int, String) =
      println("╭──────────────────────────────╮")
      println("│Select your action            │")
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
