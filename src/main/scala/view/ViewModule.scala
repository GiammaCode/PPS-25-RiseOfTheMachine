package view

import model.map.WorldMapModule.WorldMap

object ViewModule:
  trait GameView:
    def renderGameTurn(turn: Int, worldMap: WorldMap,
                       infectedCity: Int, totalCity: Int,
                       abilities: List[String], options: List[String]) : Int


  object CLIView extends GameView:

    override def renderGameTurn(turn: Int, worldMap: WorldMap,
                                infectedCity: Int, totalCity: Int,
                                abilities: List[String], options: List[String]): Int =
      renderTurn(turn)
      renderMap(worldMap)
      renderStatus(infectedCity, totalCity, abilities)
      renderActionMenu(options)

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

    private def renderStatus(infectedCity: Int, totalCity: Int, abilities: List[String]): Unit =
      val percentageDone = infectedCity.toDouble / totalCity * 100
      println(s"Infected city: $infectedCity/$totalCity --> $percentageDone%.2f%%".format(percentageDone))
      println(s"Abilities unlocked: ${abilities.mkString(", ")}\n")

    private def renderActionMenu(options: List[String]): Int =
      println("Select your action:")
      options.zipWithIndex.foreach { case (option, index) =>
        println(s"$index. $option")
      }
      print("Insert your action > ")
      scala.io.StdIn.readInt()

    def getInputForAction(options: List[String]): Int =
      renderActionMenu(options)

