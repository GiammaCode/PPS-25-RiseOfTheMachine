package controller

import controller.InputHandler.CityContext
import controller.InputHandling.InputHandlingError
import model.map.WorldMapModule.createWorldMap
import model.map.WorldState.{WorldState, createWorldState}
import model.strategy
import model.strategy.*
import model.util.GameSettings.GameSettings
import model.util.Util.doesTheActionGoesRight

object GameController:
  /**
   * Crea un nuovo controller di gioco con i componenti predefiniti
   *
   * @return Un nuovo GameController
   */
  case class GameStateImpl(worldState: WorldState,
                           humanStrategy: PlayerStrategy[HumanAction])

  private case class TurnResult(playerAction: AiAction,
                                 playerProb: Int,
                                 humanAction: Option[HumanAction])

  opaque type GameState = GameStateImpl

  import model.map.WorldMapModule.given

  def buildGameState(using GameSettings): GameState =
  GameStateImpl(
      createWorldState(createWorldMap(8),
      PlayerAI.fromStats,
      PlayerHuman.fromStats, 0),
      SmartHumanStrategy)

  import model.util.States.State.State

  private def doPlayerAction(action: AiAction, prob: Int): State[GameState, Unit] = if doesTheActionGoesRight(prob)
    then State ( gs =>
        val currentWorldState = gs.worldState
        val actionResult = currentWorldState.playerAI.executeAction(action, currentWorldState.worldMap)
        (gs.copy(worldState = currentWorldState.updatePlayer(actionResult.getPlayer).updateMap(actionResult.getCity)), ())
        )
    else State(gs => (gs,()))

  private def doHumanAction(maybeAction: Option[HumanAction]): State[GameState, Unit] = State (gs =>
    val currentWorldState = gs.worldState
    val action = maybeAction.getOrElse(gs.humanStrategy.decideAction(currentWorldState))
    val actionResult = currentWorldState.playerHuman.executeAction(action, currentWorldState.worldMap)
    val updatedState = gs.worldState.updateHuman(actionResult.getPlayer).updateMap(actionResult.getCity)
    (gs.copy(worldState = updatedState), ())
  )


  private def renderTurn(using GameSettings): State[GameState, TurnResult] = State ( gs =>
    val currentWorldState = gs.worldState.updateTurn

    import view.ViewModule.CLIView

    val ((aiChoiceIndex, aiTargetCity), humanInputOpt) = CLIView.renderGameTurn(currentWorldState)

    def resolveAction[A <: TurnAction](index: Int, city: String, options: List[A])(using InputHandler.ActionResolver[A]) =
      InputHandler.getActionFromChoice(index, CityContext(city, currentWorldState.attackableCities.map(_._1)), options)

    val playerResult = resolveAction(aiChoiceIndex, aiTargetCity, currentWorldState.playerAI.getPossibleAction)

    val humanResultOpt = humanInputOpt.map(resolveAction(_, _, currentWorldState.playerHuman.getPossibleAction))

    (playerResult, humanResultOpt) match
      case (Right(playerAction), humanOpt) if humanOpt.forall(_.isRight) =>
        val probability = currentWorldState.probabilityByCityandAction(aiTargetCity, playerAction)
        val humanActionOpt = humanOpt.flatMap(_.toOption)
        (gs.copy(worldState = currentWorldState), TurnResult(playerAction, probability, humanActionOpt))
      case _ =>
        println("Invalid input. Retrying turn.")
        renderTurn.run(gs)
  )

  def gameTurn(using GameSettings): State[GameState, Unit] =
    for
      turn <- renderTurn
      _ <- doPlayerAction(turn.playerAction, turn.playerProb)
      _ <- doHumanAction(turn.humanAction)
    yield ()

  extension(gs: GameState)
      def worldState: WorldState = gs.worldState

      def isGameOver: Boolean =
        val (gameOver,winner) = worldState.isGameOver
        import view.ViewModule.CLIView
        if gameOver then
          CLIView.renderEndGame(winner.get)
        gameOver




