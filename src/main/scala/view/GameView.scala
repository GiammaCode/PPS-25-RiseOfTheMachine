package view

trait GameView:
  def renderTurn(turn: Int): String
  //Set[(City, Set[(Int, Int)])]
  def renderMap(worldMap: Set[(String, Set[(Int, Int)])], width: Int, height: Int): String
  def renderStatus(infectedCity: Int, totalCity: Int, abilities: List[String]): String
  def renderActionMenu(options: List[String]): String

