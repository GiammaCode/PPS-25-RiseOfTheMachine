package view

import model.map.WorldMapModule.WorldMap
object ViewModule:
  trait GameView:
    def renderTurn(turn: Int): String
    def renderMap(worldMap: WorldMap): String
    def renderStatus(infectedCity: Int, totalCity: Int, abilities: List[String]): String
    def renderActionMenu(options: List[String]): String

  case class CLIView() extends GameView():
    def renderTurn(turn: Int): String = s"\n-----RISE OF THE MACHINE - TURN $turn-----\n"

    def renderMap(worldMap: WorldMap): String =
      (0 until worldMap.getSize).map { y =>
        (0 until worldMap.getSize).map { x =>
          worldMap.findInMap { case (_, coords) => coords.contains((x, y)) }
            .getOrElse(None)
        }.mkString(" ")
      }.mkString("\n")

    def renderStatus(infectedCity: Int, totalCity: Int, abilities: List[String]): String =
      val percentageDone: Double = infectedCity.toDouble / totalCity * 100
      s"Infected city: $infectedCity/$totalCity --> $percentageDone%" +
        s"\nAbilities unlocked: ${abilities.mkString(",")}\n"

    def renderActionMenu(options: List[String]): String =
      val header = "\nSelect your action:\n"
      val body = options.zipWithIndex.map { case (option, index) => s"${index + 1}. $option" }.mkString("\n")
      val footer = "\nInsert your action > "
      header + body + footer

