package model.map

import model.map.CityModule.*
import model.map.CityModule.CityImpl.*
import model.util.States.State.State
import model.util.Util.letterAt

import scala.util.Random


object WorldMapModule:
  trait CreateModuleType:
    def createMap(cityCount: Int, citySize: Int, mapSize: Int): WorldMap

  opaque type WorldMap = Set[(City, Set[(Int, Int)])]

  object DeterministicMapModule extends CreateModuleType:
    override def createMap(cityCount: Int, citySize: Int, mapSize: Int): WorldMap =
      // Pattern regolare: una città a "blocco" 2xN oppure 3xN
      def generateCityTiles(startX: Int, startY: Int, count: Int): Set[(Int, Int)] =
        (0 until count).map(i => (startX + (i % 2), startY + (i / 2))).toSet

      def positionForCity(index: Int): (Int, Int) =
        val citiesPerRow = math.max(1, mapSize / 3)
        val row = index / citiesPerRow
        val col = index % citiesPerRow
        val spacing = 3 // spaziatura costante
        (col * spacing, row * spacing)

      (0 until cityCount).map { i =>
        val name = letterAt(i)
        val city = createCity(name, citySize)
        val (startX, startY) = positionForCity(i)
        val tiles = generateCityTiles(startX, startY, citySize)
        city -> tiles
      }.toSet
  object UndeterministicMapModule extends CreateModuleType:


      opaque type RNGState[A] = State[Random, A]

      private def randomInt(max: Int): RNGState[Int] =
        State(rng => (rng, rng.nextInt(max)))


      private def shuffleList[A](list: List[A]): RNGState[List[A]] =
        State(rng => (rng, rng.shuffle(list)))


      private def generateCityTiles(start: (Int, Int), desiredSize: Int, occupied: Set[(Int, Int)], size: Int): RNGState[Set[(Int, Int)]] =
        def expand(frontier: List[(Int, Int)], current: Set[(Int, Int)]): RNGState[Set[(Int, Int)]] =
          if current.size >= desiredSize || frontier.isEmpty then State(rng => (rng, current))
          else
            val neighbors = frontier.headOption.toList.flatMap  ((x, y) =>
              List((x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1))
              ).filter { case (x, y) =>
              x >= 0 && y >= 0 && x < size && y < size &&
                !occupied.contains((x, y)) && !current.contains((x, y))
            }

            shuffleList(neighbors).flatMap { shuffled =>
              val next = shuffled.take(desiredSize - current.size)
              expand(frontier.tail ++ next, current ++ next)
            }

        expand(List(start), Set(start))

      private def generateMapState(cityCount: Int, citySize: Int, mapSize: Int): RNGState[WorldMap] =
        def loop(n: Int, acc: WorldMap, occupied: Set[(Int, Int)]): RNGState[WorldMap] =
          if n == 0 then State(r => (r, acc))
          else
            for
              x <- randomInt(mapSize)
              y <- randomInt(mapSize)
              start = (x, y)
              tiles <- generateCityTiles(start, citySize, occupied, mapSize)
              city = createCity(letterAt(n), citySize)
              next <- loop(n - 1, acc + (city -> tiles), occupied ++ tiles)
            yield next

        loop(cityCount, Set.empty, Set.empty)

      def createMap(cityCount: Int, citySize: Int, mapSize: Int): WorldMap =
        val (finalRng, worldMap) = generateMapState(cityCount, citySize, mapSize).run(Random())
        worldMap


  def createWorldMap(size: Int)(CreateMapModule: CreateModuleType): WorldMap =
    CreateMapModule.createMap(size,3,size)

  extension (worldMap: WorldMap)

    def update(): WorldMap = ???

    def getSize: Int =
      worldMap.flatMap(_._2).foldLeft(0) { case (acc, (x, y)) =>
        math.max(acc, math.max(x, y))
      } + 1
    def targetCity(name: String): City =
      worldMap.find(_._1.getName == name).get._1
    def numberOfCityInfected(): Int = worldMap.count(_._1.getOwner == Owner.AI)

    def findInMap(f: (City, Set[(Int, Int)]) => Boolean): Option[String] =
      worldMap.find { case (city, coords) => f(city, coords) }
        .flatMap { case (city, coords) => coords.headOption.map(c => city.getName) }




