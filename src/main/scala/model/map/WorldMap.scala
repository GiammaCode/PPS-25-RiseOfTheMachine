package model.map

import model.map.CityModule.*
import model.map.CityModule.CityImpl.*


object WorldMap:

  opaque type WorldMap = Set[(City, Set[(Int, Int)])]
  def createWorldMap(size:Int) : WorldMap =
    Set(( createCity("A",3),Set((0,0),(0,1),(1,0))),(createCity("B",3),Set((1,1),(2,1),(2,2))),
      ( createCity("C",3),Set((0,2),(1,2),(2,0))))

  extension (worldMap: WorldMap)
    def targetCity(name:String): City =
      worldMap.find(_._1.getName=="A").get._1





