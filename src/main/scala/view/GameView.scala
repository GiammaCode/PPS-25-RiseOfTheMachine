package view

trait GameView:
  def showTurn(turn: Int): Unit
  //Set[(City, Set[(Int, Int)])]
  def showMap(worldMap: Set[(String, Set[(Int, Int)])], width: Int, height: Int): Unit
  def showStatus(infectedCity: Int, totalCity: Int, abilities: List[String]): Unit
  def askAction(options: List[String]): Int

