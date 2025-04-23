package view

trait GameView:
  def showTurn(turn: Int): String
  //Set[(City, Set[(Int, Int)])]
  def showMap(worldMap: Set[(String, Set[(Int, Int)])], width: Int, height: Int): String
  def showStatus(infectedCity: Int, totalCity: Int, abilities: List[String]): String
  def askAction(options: List[String]): String

