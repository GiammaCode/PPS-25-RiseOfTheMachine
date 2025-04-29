package model.map

import model.map.CityModule.*
import model.map.CityModule.CityImpl.*
import model.util.States.State.State
import model.util.Util.letterAt

import scala.util.Random


object WorldMapModule:
  trait CreateModuleType:
    def createMap(size: Int): WorldMap

  opaque type WorldMap = Set[(City, Set[(Int, Int)])]

  object DeterministicMapModule extends CreateModuleType:
    override def createMap(size: Int): WorldMap =
      def generateCityTiles(startX: Int, startY: Int, count: Int): Set[(Int, Int)] =
        (0 until count).map(i => (startX + (i % 2), startY + (i / 2))).toSet

      def positionForCity(index: Int): (Int, Int) =
        val citiesPerRow = math.max(1, size / 3)
        val row = index / citiesPerRow
        val col = index % citiesPerRow
        val spacing = 3
        (col * spacing, row * spacing)

      (0 until 10).map( i =>
        val name = letterAt(i,false)
        val city = createCity(name, size,false)
        val (startX, startY) = positionForCity(i)
        val tiles = generateCityTiles(startX, startY, size)
        city -> tiles).toSet
  object UndeterministicMapModule extends CreateModuleType:

      opaque type RNGState[A] = State[Random, A]

      private def randomInt(max: Int): RNGState[Int] =
        State(rng => (rng, rng.nextInt(max)))


      private def shuffleList[A](list: List[A]): RNGState[List[A]] =
        State(rng => (rng, rng.shuffle(list)))


      private def randomCitySize(isCapital:Boolean): RNGState[Int] =
        if isCapital
        then randomInt(3).map(_ + 7)
        else randomInt(3).map(_ + 3)

      private def validNeighbors(tiles: Iterable[(Int, Int)], size: Int, exclude: Set[(Int, Int)] = Set.empty): List[(Int, Int)] =
        tiles.toList
          .flatMap((x, y) => List((x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)))
          .filter((x, y) =>
            x >= 0 && y >= 0 && x < size && y < size &&
              !exclude.contains((x, y))
          )
          .distinct

      private def adjacentTo(tiles: Set[(Int, Int)], size: Int): List[(Int, Int)] =
        validNeighbors(tiles, size, tiles)

      private def generateCityTiles(start: (Int, Int), desiredSize: Int, occupied: Set[(Int, Int)], size: Int): RNGState[Set[(Int, Int)]] =
        def expand(frontier: List[(Int, Int)], current: Set[(Int, Int)]): RNGState[Set[(Int, Int)]] =
          if current.size >= desiredSize || frontier.isEmpty then State(rng => (rng, current))
          else
            val neighbors = validNeighbors(frontier.headOption.toList, size, occupied ++ current)
            shuffleList(neighbors).flatMap(shuffled =>
              val next = shuffled.take(desiredSize - current.size)
              expand(frontier.tail ++ next, current ++ next)
            )
        expand(List(start), Set(start))

      private def chooseStartingPoint(acc: WorldMap, occupied: Set[(Int, Int)], mapSize: Int): RNGState[(Int, Int)] =
        if acc.isEmpty then
          for
            x <- randomInt(mapSize)
            y <- randomInt(mapSize)
          yield (x, y)
        else
          val allCoords = acc.flatMap(_._2)
          val possibleStarts = adjacentTo(allCoords, mapSize).filterNot(occupied.contains)
          if possibleStarts.isEmpty then
            State(rng => (rng, allCoords.head)) // fallback se non ci sono spazi adiacenti
          else
            shuffleList(possibleStarts).map(_.head)

      private def generateMapState(mapSize: Int, capitalCount: Int): RNGState[WorldMap] =
        def loop(n: Int, capitalsLeft: Int, acc: WorldMap, occupied: Set[(Int, Int)]): RNGState[WorldMap] =
          val remainingTiles = mapSize * mapSize - occupied.size
          if remainingTiles <= 1 then State(rng => (rng, acc))
          else
            val isCapital = capitalsLeft > 0
            for
              size <- randomCitySize(isCapital)
              start <- chooseStartingPoint(acc, occupied, mapSize)
              tiles <- generateCityTiles(start, size, occupied, mapSize)
              city = createCity(letterAt(n, isCapital), size,isCapital)
              next <- loop(n + 1, if isCapital then capitalsLeft - 1 else capitalsLeft, acc + (city -> tiles), occupied ++ tiles)
            yield next
        loop(0, capitalCount, Set.empty, Set.empty)


      def createMap(citySize: Int): WorldMap =
        val (finalRng, worldMap) = generateMapState(citySize,5).run(Random(5))
        worldMap


  def createWorldMap(size: Int)(CreateMapModule: CreateModuleType): WorldMap =
    CreateMapModule.createMap(size)

  extension (worldMap: WorldMap)

    def changeACityOfTheMap(city: City): WorldMap =
        worldMap.find(_._1.getName == city.getName)
        .map ((_, coords) => worldMap.filterNot(_._1.getName == city.getName) + (city -> coords))
        .getOrElse(worldMap)

    def getSize: Int = worldMap.flatMap(_._2).foldLeft(0)((acc, xy) => math.max(acc, math.max(xy._1, xy._2))) + 1

    def targetCity(name: String): City =
      worldMap.find(_._1.getName == name).get._1

    def numberOfCityInfected(): Int = worldMap.count(_._1.getOwner == Owner.AI)

    def findInMap(f: (City, Set[(Int, Int)]) => Boolean): Option[String] =
      worldMap.find(f.tupled).flatMap((city, coords) => coords.headOption.map(_ => city.getName))

    def renderList(): Unit =
      worldMap.foreach { case (city, coords) =>
        val coordsStr = coords.toList.sortBy(_._1).mkString(", ")
        println(s"- ${city.getName}: $coordsStr")
      }

    def cities: Set[(City, Set[(Int, Int)])] = worldMap




