package model

import model.MenageCity.*
class WorldMap(size: Int):

  private var worldMap: Set[(City, Set[(Int, Int)])] = createMap(size)
  private def createMap(size:Int) : Set[(City,Set[(Int,Int)])] =
    Set((City("A",3),Set((0,0),(0,1),(1,0))),(City("B",3),Set((1,1),(2,1),(2,2))),
      (City("C",3),Set((0,2),(1,2),(2,0))))


  def targetCity(name:String): City =
    worldMap.find(_._1.getName=="A").get._1





