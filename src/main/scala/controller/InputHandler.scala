package controller

import InputHandling.{InputHandlingError, InputParsingError, InvalidChoice}
import model.strategy.*
import model.strategy.{AiAction, HumanAction}

/** Handles errors during user input parsing or validation. */
object InputHandling:

  sealed trait InputHandlingError
  case class InvalidChoice(choice: Int, availableRange: Range) extends InputHandlingError
  case class InputParsingError(input: String, message: String) extends InputHandlingError

  object InputHandlingError:
    extension (e: InputHandlingError)
      def userMessage: String = e match
        case InvalidChoice(choice, range) =>
          s"Invalid choice: $choice. Please enter a number between ${range.min} and ${range.max}."
        case InputParsingError(input, msg) =>
          s"Input format error: $input. $msg"

object InputHandler:

  /** Context used for action resolution */
  sealed trait ActionContext
  case class CityContext(cityName: String, applicableCities: Set[String]) extends ActionContext
  case object NoContext extends ActionContext

  /** Typeclass for resolving a base action using context into a fully initialized one */
  trait ActionResolver[A <: TurnAction]:
    def resolve(base: A, context: ActionContext): Either[InputHandlingError, A]

  /** Generic handler to convert a choice into a validated action */
  def getActionFromChoice[A <: TurnAction](
                                            choice: Int,
                                            context: ActionContext,
                                            availableActions: List[A]
                                          )(using resolver: ActionResolver[A]): Either[InputHandlingError, A] =
    for {
      action <- availableActions.lift(choice)
        .toRight(InvalidChoice(choice, 0 to availableActions.size - 1))
      resolved <- resolver.resolve(action, context)
    } yield resolved

  /** Resolver for AiActions */
  given aiActionResolver: ActionResolver[AiAction] with
    def resolve(action: AiAction, ctx: ActionContext): Either[InputHandlingError, AiAction] =
      ctx match
        case CityContext(cityName, applicableCities) =>
          action match
            case _: Sabotage | _: Infect =>
              Either.cond(
                applicableCities.contains(cityName),
                action match
                  case _: Sabotage => Sabotage(List(cityName))
                  case _: Infect   => Infect(List(cityName))
                  case _ => action,
                InputParsingError(cityName, s"The city '$cityName' is not attackable.")
              )
            case Evolve => Right(Evolve)
        case _ =>
          Left(InputParsingError("Context", "Invalid context provided for AiAction."))

  /** Resolver for HumanActions */
  given humanActionResolver: ActionResolver[HumanAction] with
    def resolve(action: HumanAction, ctx: ActionContext): Either[InputHandlingError, HumanAction] =
      ctx match
        case CityContext(cityName, ownedCities) =>
          val targets = List(cityName) // TODO: owned cities become the target !!!!!!!!!!!!!!!!!!
          action match
            case _: CityDefense =>
              Either.cond(
                targets.nonEmpty,
                CityDefense(targets),
                InputParsingError("Targets", "Some targets are not your cities.")
              )
            case _: GlobalDefense =>
              Either.cond(
                targets.nonEmpty,
                GlobalDefense(targets),
                InputParsingError("Targets", "Some targets are not your cities.")
              )
            case DevelopKillSwitch => Right(DevelopKillSwitch)
        case _ =>
          Left(InputParsingError("Context", "Invalid context provided for HumanAction."))
