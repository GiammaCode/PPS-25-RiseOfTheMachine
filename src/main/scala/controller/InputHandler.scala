package controller

import InputHandling.{InputHandlingError, InvalidChoice}
import model.strategy.AiAction
// --- Input Error Handling Module ---

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
          // Assumes that the range is 1-based for user message (range.min/max on Range are inclusive)
          s"Invalid choice: ${choice}. Please enter a number between ${range.min} and ${range.max}."
        case InputParsingError(input, msg) => s"Input format error: '${input}'. You must enter a valid number."


// --- Input Handler Module (Pure Logic for Mapping) ---

object InputHandler:

  /** A pure object responsible for mapping the user's numeric input to a valid game action. */
  def getActionFromChoice(
                           choice: Int,
                           availableActions: List[AiAction]
                         ): Either[InputHandlingError, AiAction] = {
    // Check if there are any available actions
    if (availableActions.isEmpty) {
      Left(InvalidChoice(choice, 1 to 0)) // Return error if no actions are available
    } else {
      // Map the numeric choice (1-based) to a 0-based index
      val index = choice - 1
      // Try to find the action corresponding to the chosen number, return error if not found
      availableActions.lift(index)
        .toRight(InvalidChoice(choice, Range(1, availableActions.size)))
    }
  }
