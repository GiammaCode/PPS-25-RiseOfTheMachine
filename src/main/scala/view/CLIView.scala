package view

case class CLIView() extends GameView():
  def showTurn(turn: Int): Unit =
    println(s"\n-----RISE OF THE MACHINE - TURN $turn-----\n")
  def showMap(worldMap: Set[(String, Set[(Int, Int)])], width: Int, height: Int): Unit =
    val grid = Array.fill(height, width)(" ")
    for (city, coords) <- worldMap do
      for (x,y) <- coords do
        grid(x)(y) = city

    for (y <- 0 until height) do
      val row = grid(y).mkString(" ")
      println(s"$row")
  def showStatus(infectedCity: Int, totalCity: Int, abilities: List[String]): Unit =
    val percentageDone : Int = totalCity / infectedCity
    println(s"Infected city: $infectedCity/$totalCity --> $percentageDone" +
      s"\nAbilities unlocked: ${abilities.mkString(",")}")

  def askAction(options: List[String]): Int =
    println("select your action:")
    options.zipWithIndex.foreach { case (option,index) =>
      println(s"$index. $option")
    }
    println("Insert your action > ")
    scala.io.StdIn.readInt()

@main def CliViewMain(): Unit =
  val worldMap: Set[(String, Set[(Int, Int)])] = Set(
    ("A", Set((0, 0), (1, 0), (2, 0), (3, 0), (4, 0), (5, 0), (0, 1), (1, 1), (2, 1), (3, 1))),
    ("B", Set((6, 0), (7, 0), (8, 0), (9, 0), (6, 1), (7, 1), (8, 1), (9, 1), (4, 1), (5, 1))),
    ("C", Set((0, 2), (1, 2), (2, 2), (3, 2), (4, 2), (5, 2), (0, 3), (1, 3), (2, 3), (3, 3))),
    ("D", Set((6, 2), (7, 2), (8, 2), (9, 2), (6, 3), (7, 3), (8, 3), (9, 3), (4, 3), (5, 3))),
    ("E", Set((0, 4), (1, 4), (2, 4), (3, 4), (0, 5), (1, 5), (2, 5), (3, 5), (4, 4), (5, 4))),
    ("F", Set((4, 5), (5, 5), (6, 4), (7, 4), (6, 5), (7, 5), (8, 4), (9, 4), (8, 5), (9, 5))),
    ("G", Set((0, 6), (1, 6), (0, 7), (1, 7), (2, 6), (2, 7), (3, 6), (3, 7), (4, 6), (4, 7))),
    ("H", Set((5, 6), (6, 6), (7, 6), (5, 7), (6, 7), (7, 7), (8, 6), (9, 6), (8, 7), (9, 7))),
    ("I", Set((0, 8), (1, 8), (2, 8), (3, 8), (4, 8), (5, 8), (6, 8), (0, 9), (1, 9), (2, 9))),
    ("J", Set((3, 9), (4, 9), (5, 9), (6, 9), (7, 8), (8, 8), (9, 8), (7, 9), (8, 9), (9, 9)))
  )

  val view  = CLIView()
  val abilities = List("ab1", "ab2")
  val options = List("Infect adjacent city", "Sabotages adjacent city",
  "Evolve new capacity", "Auto -shift", "Exit")

  view.showTurn(5)
  view.showStatus(3,15, abilities)
  view.showMap(worldMap, 10, 10)
  val opt = view.askAction(options)

  print(s"option choosed: $opt")


