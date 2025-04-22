package view

trait GameView:
  def showTurn(turn: Int): Unit

  //showMap(map: MapGrid)
  def showMap(): Unit

  //to decide arguments
  def showStatus(): Unit
  def askAction(option: List[String]): Unit
  def showMessages(msg: String): Unit

