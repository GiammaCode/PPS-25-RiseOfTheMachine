package view

import model.map.WorldMapModule.WorldMap
import model.map.WorldState.WorldState
import model.strategy.AiAbility.AiAbility
import model.strategy.*
import model.util.GameSettings.*
import view.ViewModule.GameTurnInput.GameTurnInput

import scala.annotation.tailrec
import scala.io.StdIn

/**
 * ViewModule contains the trait for abstracting the game UI,
 * and a concrete implementation for command-line interaction (CLIView).
 */
object ViewModule:

  object GameTurnInput:
    opaque type GameTurnInput = ((Int, String), Option[(Int, String)])

    def apply(aiInput: (Int, String), humanInput: Option[(Int, String)]): GameTurnInput = (aiInput, humanInput)

    extension (input: GameTurnInput)
      def aiInput: (Int, String) = input._1
      def humanInput: Option[(Int, String)] = input._2

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
    def renderGameTurn(worldState: WorldState)(using GameSettings): GameTurnInput

    def renderEndGame(winner: PlayerEntity): Unit

  /**
   * CLIView is the command-line implementation of GameView.
   * It renders all game content to the terminal and handles user input via StdIn.
   */
  object CLIView extends GameView:
    import CLIFormatter.*

    /**
     * Prompts the player to choose the game mode, then (if Singleplayer)
     * also requests the difficulty level.
     *
     * @return a tuple containing the chosen GameMode and Difficulty
     */
    override def renderGameModeMenu(): GameSettings =
      printAsciiTitle("RISE OF THE MACHINE")
      val mode = askGameMode()
      val selectedDifficulty: Difficulty = mode match
        case GameMode.Singleplayer =>
          printBoxedMenu("📊 Select difficulty level", List("Easy", "Normal", "Hard"))
          StdIn.readLine().trim match
            case "0" => Difficulty.Easy
            case "1" => Difficulty.Normal
            case "2" => Difficulty.Hard
            case _ =>
              println("Invalid input. Defaulting to Normal difficulty")
              Difficulty.Normal

        case GameMode.Multiplayer => Difficulty.Normal

      forSettings(mode, selectedDifficulty)

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
    override def renderGameTurn(worldState: WorldState)(using gameSettings: GameSettings): GameTurnInput =
      renderTurn(worldState.turn)
      renderMap(worldState.worldMap, worldState.playerAI.conqueredCities)
      renderStatus(worldState.infectionState, worldState.AIUnlockedAbilities, worldState.playerHuman.killSwitch)
      renderProbability(worldState.attackableCities)
      renderComplessiveAction(worldState.playerHuman, worldState.playerAI)
      gameSettings.gameMode match
        case GameMode.Singleplayer =>
          val aiMove = renderActionMenu("AI PLAYER TURN", worldState.AiOptions)
          GameTurnInput(aiMove, None)

        case GameMode.Multiplayer =>
          val aiMove = renderActionMenu("AI PLAYER TURN", worldState.AiOptions)
          val humanMove = renderActionMenu("HUMAN PLAYER TURN", worldState.HumanOptions)
          GameTurnInput(aiMove, Some(humanMove))

    /**
     * Displays the end-game message based on the winning player.
     *
     * This method takes a `PlayerEntity` (either a `PlayerHuman` or `PlayerAI`) and prints
     * a corresponding message to indicate who won the game.
     *
     * @param winner the player entity that has fulfilled the victory condition (AI or Human)
     */
    override def renderEndGame(winner: PlayerEntity): Unit = winner match
      case _: PlayerHuman =>
        printBoxedContent("🌍  HUMANS SAVED THE WORLD!",
          List("✅ The kill switch was activated.",
            "\uD83E\uDDEC Humanity survives... for now."
          ))
      case _: PlayerAI =>
        printBoxedContent("🤖  AI CONQUERED THE WORLD!", List(
          "💥 The world has fallen.",
          "🔒 Resistance was futile"
        ))

    /**
     * Recursively displays the game mode selection menu and handles user input.
     *
     * If "Tutorial" is selected, the tutorial content is shown and the menu is re-displayed.
     * If "Exit" is selected, the application terminates.
     * If an invalid input is entered, the menu is shown again.
     *
     * The method guarantees that a valid GameMode is returned.
     *
     * @return the selected GameMode (Singleplayer or Multiplayer)
     */
    @tailrec
    private def askGameMode(): GameMode =
      printBoxedMenu("📊 Select game mode", List("Single Player", "Multiplayer", "Tutorial", "Exit"))
      StdIn.readLine().trim match
        case "0" => GameMode.Singleplayer
        case "1" => GameMode.Multiplayer
        case "2" =>
          renderTutorial
          println("\nPress ENTER to continue...")
          StdIn.readLine()
          askGameMode()
        case "3" =>
          println("Exiting game. Goodbye!")
          sys.exit(0)
        case _ =>
          println("Invalid input. Please try again.")
          askGameMode()

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
    private def renderMap(worldMap: WorldMap, AIconqueredCities: Set[String]): Unit =
      println("    🗺️  World Map")
      printMap(worldMap, AIconqueredCities)

    /**
     * Displays the infection progress and unlocked abilities of the AI.
     *
     * @param infectionState tuple of (infected cities, total cities)
     * @param abilities      the currently unlocked AiAbilities
     */
    private def renderStatus(infectionState: (Int, Int), abilities: Set[AiAbility], killSwitch: Int): Unit =
      val percentageInfected = (infectionState._1.toDouble / infectionState._2 * 100).toInt
      val abilitiesOutput = if abilities.nonEmpty then abilities.mkString(", ") else "0 unlocked"
      val killSwitchProgress = s"$killSwitch%"
      printBoxedContent("📊 Statistics",
        List(f"🦠 Infected Cities:  $percentageInfected%3d%%",
          f"🤖 AI Abilities:        ${abilitiesOutput.padTo(25, ' ')}",
          f"🧪 Develop KillSwitch:  ${killSwitchProgress.padTo(25, ' ')}"
        ))

    /**
     * Renders the infection and sabotage probabilities for each attackable city.
     *
     * @param cities a set of tuples (cityName, infectionChance, sabotageChance)
     */
    private def renderProbability(cities: Set[(String, Int, Int)]): Unit =
      if cities.nonEmpty then
        val formatted = cities.toSeq
          .sortBy(_._1)
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

      printBoxedContent("🧾 Action Summary (last 7 actions)", List(
        s"Human: ${human.executedActions.take(7).map(formatAction).mkString(" || ")}",
        s"AI   : ${ai.executedActions.take(7).map(formatAction).mkString(" || ")}"
      ))

    /**
     * Renders a stylized menu of available actions to the terminal.
     * Allows the user to input an action index and optionally a city name.
     *
     * @param options the list of action names to display
     * @return a tuple (actionIndex, targetCityName)
     */
    private def renderActionMenu(player: String, options: List[String]): (Int, String) =
      printBoxedMenu(player, options)
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

    /**
     * Renders a stylized game's tutorial
     * Print a box with instructions
     */
    private def renderTutorial: Unit =
      printBoxedContent("TUTORIAL – RISE OF THE MACHINE", List(
        "Welcome! This tutorial will guide you through your first steps in the game.",
        "",
        "🎮 GAME MODE:",
        "At startup, select the game mode:",
        "  0. Single Player",
        "  1. Multiplayer",
        "  2. Tutorial",
        "  3. Exit",
        "Default chosen is Single Player.",
        "",
        "🎯 DIFFICULTY LEVEL:",
        "After selecting the mode, choose a difficulty:",
        "  0. Easy    → Passive AI",
        "  1. Normal  → Balanced challenge",
        "  2. Hard    → Aggressive and strategic AI",
        "Default chosen is Normal difficulty.",
        "",
        "🌍 WORLD MAP:",
        "A grid displays the world map with city letters as identifiers.",
        "",
        "📊 STATISTICS:",
        "- Infected cities",
        "- Unlocked AI abilities",
        "- Human KillSwitch progress",
        "",
        "📈 ATTACKABLE CITIES:",
        "For each city, you can see:",
        "  💉 Infection success chance",
        "  🛠️  Sabotage success chance",
        "",
        "🧠 HUMAN TURN:",
        "Available actions:",
        "  1. Defend vulnerable cities",
        "  2. Launch global defense",
        "  3. Develop the KillSwitch",
        "",
        "🤖 AI TURN:",
        "Available actions:",
        "  1. Infect to conquer a city",
        "  2. Sabotage to weaken a city",
        "  3. Evolve to unlock new abilities",
        "",
        "🏁 WIN CONDITIONS:",
        "- The AI wins if it infects 50% of cities or conquers 3 capitals",
        "- The human wins if the KillSwitch reaches 90% progress",
        "",
        "📝 TIP:",
        "Check success rates, defend key cities, and adapt your strategy!"
      ))
