package view

import model.map.WorldMapModule.WorldMap
import model.map.WorldState.WorldState
import model.strategy.AiAbility.AiAbility


object ViewModule:
  trait GameView:
    def renderGameTurn(worldState: WorldState) : Int

  object CLIView extends GameView:

    override def renderGameTurn(worldState: WorldState): Int =
      renderTurn(worldState.turn)
      renderMap(worldState.worldMap)
      renderStatus(worldState.infectionState, worldState.AIUnlockedAbilities)
      renderActionMenu(worldState.options)

    private def renderTurn(turn: Int): Unit =
      println(s"\n-----RISE OF THE MACHINE - TURN $turn-----\n")

    private def renderMap(worldMap: WorldMap): Unit =
      val mapString = (0 until worldMap.getSize).map { y =>
        (0 until worldMap.getSize).map { x =>
          worldMap.findInMap { case (_, coords) => coords.contains((x, y)) }.
            getOrElse("x")
        }.mkString(" ")
      }.mkString("\n")
      println(mapString)

    private def renderStatus(infectionState: (Int, Int), abilities: Set[AiAbility]): Unit =
      val percentageDone = infectionState._1.toDouble / infectionState._2 * 100
      println(s"Infected city: ${infectionState._1}/${infectionState._2} --> $percentageDone%.2f%%".format(percentageDone))
      println(s"Abilities unlocked: NEED TO INSERT ABILITY . tostring\n")

    private def renderActionMenu(options: List[String]): Int =
      println("Select your action:")
      options.zipWithIndex.foreach { case (option, index) =>
        println(s"$index. $option")
      }
      print("Insert your action > ")
      scala.io.StdIn.readInt()

    def getInputForAction(options: List[String]): Int =
      renderActionMenu(options)

