package model.map

import model.map.CityModule.*
import model.map.CityModule.CityImpl.*
import model.util.States.State.State
import model.util.Util.{doesTheActionGoesRight, letterAt}

import scala.annotation.tailrec
import scala.util.Random


object WorldMapModule:
  trait CreateModuleType:
    def createMap(size: Int): WorldMap

  opaque type WorldMap = Set[(City, Set[(Int, Int)])]

  object DeterministicMapModule extends CreateModuleType:
    override def createMap(size: Int): WorldMap =
      def allTiles: Set[(Int, Int)] =
        (for x <- 0 until size; y <- 0 until size yield (x, y)).toSet

      def adjacentTo(tiles: Set[(Int, Int)]): Set[(Int, Int)] =
        tiles.flatMap { case (x, y) =>
          Set((x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1))
        }.filter { case (x, y) =>
          x >= 0 && y >= 0 && x < size && y < size
        }

      def expandCity(start: (Int, Int), available: Set[(Int, Int)], desiredSize: Int): Set[(Int, Int)] =
        @tailrec
        def loop(frontier: List[(Int, Int)], visited: Set[(Int, Int)]): Set[(Int, Int)] =
          if visited.size >= desiredSize || frontier.isEmpty then visited
          else
            val next = frontier.head
            val newNeighbors = adjacentTo(Set(next)).intersect(available).diff(visited)
            loop(frontier.tail ++ newNeighbors, visited + next)

        loop(List(start), Set(start)).intersect(available)

      @tailrec
      def loop(index: Int, available: Set[(Int, Int)], acc: Set[(City, Set[(Int, Int)])]): Set[(City, Set[(Int, Int)])] =
        if available.isEmpty then acc
        else
          val isCapital = index < 5
          val name = letterAt(index, isCapital)
          val city = createCity(name, size, isCapital)
          val start = available.head
          val maxCitySize = math.min(10, available.size)
          val tiles = expandCity(start, available, maxCitySize)
          if tiles.size < 0 then loop(index + 1, available -- tiles, acc) // ignora città troppo piccole
          else loop(index + 1, available -- tiles, acc + (city -> tiles))

      loop(0, allTiles, Set.empty)

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
          if remainingTiles == 0  then State(rng => (rng, acc))
          else
            val isCapital = capitalsLeft > 0 && doesTheActionGoesRight(50)
            for
              size <- randomCitySize(isCapital)
              start <- chooseStartingPoint(acc, occupied, mapSize)
              tiles <- generateCityTiles(start, size, occupied, mapSize)
              city = createCity(letterAt(n, isCapital), size,isCapital)
              next <- loop(n + 1, if isCapital then capitalsLeft - 1 else capitalsLeft, acc + (city -> tiles), occupied ++ tiles)
            yield next
        loop(0, capitalCount, Set.empty, Set.empty)


      def createMap(citySize: Int): WorldMap =
        val (finalRng, worldMap) = generateMapState(citySize,citySize/2).run(Random())
        worldMap

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

  def createWorldMap(size: Int)(CreateMapModule: CreateModuleType): WorldMap =
    CreateMapModule.createMap(size)

  extension (worldMap: WorldMap)

    def changeACityOfTheMap(city: City): WorldMap =
        worldMap.find(_._1.getName == city.getName)
        .map ((_, coords) => worldMap.filterNot(_._1.getName == city.getName) + (city -> coords))
        .getOrElse(worldMap)

    def getSize: Int = worldMap.flatMap(_._2).foldLeft(0)((acc, xy) => math.max(acc, math.max(xy._1, xy._2))) + 1

    def getCityByName(name: String): Option[City] =
      worldMap.find(_._1.getName == name).map(_._1)

    def numberOfCityInfected(): Int = worldMap.count(_._1.getOwner == Owner.AI)

    def numberOfCity(): Int = worldMap.size

    def findInMap(f: (City, Set[(Int, Int)]) => Boolean): Option[String] =
      worldMap.find(f.tupled).flatMap((city, coords) => coords.headOption.map(_ => city.getName))


    def cities: Set[(City, Set[(Int, Int)])] = worldMap

    def AiCities: Set[City] = worldMap.filter(_._1.getOwner == Owner.AI).map(_._1)

    def HumanCities: Set[City] = worldMap.filter(_._1.getOwner == Owner.HUMAN).map(_._1)

    def getAdjacentCities: Set[City] =
      if AiCities.isEmpty then HumanCities
      else worldMap.collect:
        case (city, coords)
          if city.getOwner == Owner.HUMAN &&
            coords.exists(adjacentTo(worldMap.filter(_._1.getOwner == Owner.AI).flatMap(_._2), worldMap.getSize).toSet.contains)
        => city






