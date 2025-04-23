package view

case class CLIView() extends GameView():
  def renderTurn(turn: Int): String = s"\n-----RISE OF THE MACHINE - TURN $turn-----\n"

  def renderMap(worldMap: Set[(String, Set[(Int, Int)])], width: Int, height: Int): String =
    (0 until height).map { y =>
      (0 until width).map { x =>
        worldMap.find { case (_, coords) => coords.contains((x, y)) }
          .get._1
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

