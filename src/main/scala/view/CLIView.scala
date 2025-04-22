package view

case class CLIView() extends GameView():
  def showTurn(turn: Int): Unit =
    println(s"\n-----RISE OF THE MACHINE - TURN $turn-----\n")

  //showMap(map: MapGrid)
  def showMap(): Unit = ???

  //to decide arguments
  def showStatus(): Unit = ???

  def askAction(option: List[String]): Unit = ???

  def showMessages(msg: String): Unit = ???


