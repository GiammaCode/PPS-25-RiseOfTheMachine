package model.map

import model.map.CityModule.*
import model.map.CityModule.CityImpl.*



object WorldMapModule:
    trait CreateModuleType:
      def createMap(size: Int): WorldMap

    opaque type WorldMap = Set[(City, Set[(Int, Int)])]

    object DeterministicMapModule extends CreateModuleType:
      def createMap(size:Int): WorldMap =
        Set((createCity("A", 3), Set((0, 0), (0, 1), (1, 0))),
          (createCity("B", 3), Set((1, 1), (2, 1), (2, 2))),
          (createCity("C", 3), Set((0, 2), (1, 2), (2, 0))))

    object UndeterministicMapModule extends CreateModuleType:
      private val rand = scala.util.Random
      def createMap(size: Int): WorldMap =
        var occupied: Set[(Int, Int)] = Set()
        var cityCount = 0
        var world: Set[(City, Set[(Int, Int)])] = Set()

        def isValid(cell: (Int, Int)): Boolean =
          cell._1 >= 0 && cell._1 < size && cell._2 >= 0 && cell._2 < size && !occupied.contains(cell)

        def adjacent(cell: (Int, Int)): List[(Int, Int)] =
          List(
            (cell._1 + 1, cell._2), (cell._1 - 1, cell._2),
            (cell._1, cell._2 + 1), (cell._1, cell._2 - 1)
          ).filter(isValid)

        while occupied.size < size * size - 6 do
          val citySize = 3 + rand.nextInt(4) // 3 to 6 cells
          val startCell = (rand.nextInt(size), rand.nextInt(size))
          if isValid(startCell) then
            var cityCells: Set[(Int, Int)] = Set(startCell)
            var frontier = List(startCell)

            while cityCells.size < citySize && frontier.nonEmpty do
              val current = frontier.head
              frontier = frontier.tail
              val options = adjacent(current).filterNot(cityCells.contains)
              val selected = rand.shuffle(options).take(citySize - cityCells.size)
              cityCells ++= selected
              frontier ++= selected

            if cityCells.size == citySize then
              occupied ++= cityCells
              val name = ('A' + cityCount).toChar.toString
              val city = createCity(name, citySize)
              world += (city -> cityCells)
              cityCount += 1

        world

    def createWorldMap(size:Int)( CreateMapModule: CreateModuleType) : WorldMap =
      CreateMapModule.createMap(size)

    extension (worldMap: WorldMap)

      def update(): WorldMap = ???
      def targetCity(name:String): City =
      worldMap.find(_._1.getName=="A").get._1
      def numberOfCityInfected(): Int = worldMap.count(_._1.getOwner == Owner.AI)

      def findInMap(f: (City, Set[(Int, Int)]) => Boolean): Option[String] =
        worldMap.find { case (city, coords) => f(city, coords) }
        .flatMap { case (city, coords) => coords.headOption.map(c => city.getName)}
      def renderMap (): Set[String] =worldMap.map(_._1.getName);








