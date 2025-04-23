package view

case class CLIView() extends GameView():
  def renderTurn(turn: Int): String = s"\n-----RISE OF THE MACHINE - TURN $turn-----\n"

  def renderMap(worldMap: Set[(String, Set[(Int, Int)])], width: Int, height: Int): String =
    val grid = Array.fill(height, width)(" ")
    for (city, coords) <- worldMap do
      for (x, y) <- coords do
        grid(x)(y) = city
    grid.map(_.mkString(" ")).mkString("\n")

  def renderStatus(infectedCity: Int, totalCity: Int, abilities: List[String]): String =
    val percentageDone: Double = infectedCity.toDouble / totalCity * 100
    s"Infected city: $infectedCity/$totalCity --> $percentageDone%" +
      s"\nAbilities unlocked: ${abilities.mkString(",")}\n"

  def renderActionMenu(options: List[String]): String =
    "\nSelect your action:\n" +
      options.zipWithIndex.map { case (option, index) => s"${index + 1}. $option" }.mkString("\n") +
      "\nInsert your action > "



