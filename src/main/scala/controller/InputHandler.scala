package controller

import InputHandling.{InputHandlingError, InputParsingError, InvalidChoice}
import model.map.WorldMapModule
import model.strategy
import model.strategy.{AiAction, Evolve, Infect, Sabotage}

object InputHandling:

  /** Error types that may occur during the interpretation or parsing of user input. */
  sealed trait InputHandlingError
  /** Numeric choice out of the available options range. */
  case class InvalidChoice(choice: Int, availableRange: Range) extends InputHandlingError
  /** Error in converting raw input (e.g., non-numeric string) - handled by the View. */
  case class InputParsingError(input: String, message: String) extends InputHandlingError

  /** Helper methods for input errors */
  object InputHandlingError:
    extension (e: InputHandlingError)
      /** Generates a user-friendly message based on the error type. */
      def userMessage: String = e match
        case InvalidChoice(choice, range) =>
          s"Invalid choice: $choice. Please enter a number between ${range.min} and ${range.max}."
        case InputParsingError(input, msg) => s"Input format error: $input. You must enter a valid number."


object InputHandler:

  /** A pure object responsible for mapping the user's numeric input to a valid game action. */
  def getActionFromChoice(
                           choice: Int,
                           cityName: String,
                           attackableCities : Set[String],
                           availableActions: List[AiAction]
                         ): Either[InputHandlingError, AiAction] =
    for {
      _ <- Either.cond(
        attackableCities.contains(cityName),
        (),
        InputParsingError(cityName, s"The city '$cityName' is not attackable.")
      )

      action <- availableActions.lift(choice - 1)
        .toRight(InvalidChoice(choice, 1 to availableActions.size))

    } yield action match
      case _: Sabotage => Sabotage(List(cityName))
      case _: Infect => Infect(List(cityName))
      case  Evolve => Evolve
