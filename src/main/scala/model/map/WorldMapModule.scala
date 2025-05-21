package model.map

import model.map.CityModule.*
import model.map.CityModule.CityImpl.*
import model.util.States.State.State
import model.util.Util.{doesTheActionGoesRight, letterAt}

import scala.annotation.tailrec
import scala.util.Random

/**
 * == WorldMapModule ==
 *
 * This module defines the representation and construction logic for the game map.
 * A world map consists of cities, each associated with a set of coordinate tiles representing its spatial footprint.
 *
 * === Opaque Type ===
 * - [[WorldMap]] is an abstracted representation of a set of cities with their tile coordinates.
 *
 *
 * == Traits ==
 * === CreateModuleType ===
 * Abstract interface for any map creation strategy.
 *
 * == DeterministicMapModule ==
 * Creates a reproducible, grid-based map with adjacent tile allocation for each city.
 *
 * == UndeterministicMapModule ==
 * Uses a state monad with a PRNG to construct a randomized map, including random tile distribution and city sizes.
 *
 * == Extension Methods ==
 * Adds useful methods to `WorldMap`:
 *  - `getSizeOfTheMap`: returns map dimensions
 *  - `getCityByName(name)`: looks up a city by name
 *  - `numberOfCityInfected`: counts infected (AI-controlled) cities
 *  - `getAdjacentCities`: finds human cities adjacent to infected ones
 *  - `changeACityOfTheMap`: replaces a city with an updated version
 */

object WorldMapModule:

  /**
   * Opaque type representing the entire game map as a set of pairs:
   * - City data
   * - Set of coordinates (tiles) associated with the city
   */

  opaque type WorldMap = Set[(City, Set[(Int, Int)])]

  /**
   * Strategy trait for creating a world map.
   */
  trait CreateModuleType:

    /**
     * Creates a world map of a given size.
     *
     * @param size the dimension (width/height) of the map
     * @return a new WorldMap instance
     */
    def createMap(size: Int): WorldMap

  /**
   * Implementation of [[CreateModuleType]] using deterministic tile allocation.
   * Cities are created in a grid-like fashion.
   */
  object DeterministicMapModule extends CreateModuleType:
    override def createMap(size: Int): WorldMap =

      def allTiles: Set[(Int, Int)] =
        (for x <- 0 until size; y <- 0 until size yield (x, y)).toSet

      def expandCity(start: (Int, Int), available: Set[(Int, Int)], desiredSize: Int): Set[(Int, Int)] =
        @tailrec
        def expandLoop(frontier: List[(Int, Int)], visited: Set[(Int, Int)]): Set[(Int, Int)] =
          if visited.size >= desiredSize || frontier.isEmpty then visited
          else
            val next = frontier.head
            val newNeighbors = adjacentTo(Set(next),size).toSet.intersect(available).diff(visited)
            expandLoop(frontier.tail ++ newNeighbors, visited + next)

        expandLoop(List(start), Set(start)).intersect(available)

      @tailrec
      def createMapLoop(index: Int, available: Set[(Int, Int)], acc: Set[(City, Set[(Int, Int)])]): Set[(City, Set[(Int, Int)])] =
        if available.isEmpty then acc
        else
          val isCapital = index < 5
          val name = letterAt(index, isCapital)
          val city = createCity(name, size, isCapital)
          val start = available.head
          val maxCitySize = math.min(10, available.size)
          val tiles = expandCity(start, available, maxCitySize)
          if tiles.size < 0 then createMapLoop(index + 1, available -- tiles, acc)
          else createMapLoop(index + 1, available -- tiles, acc + (city -> tiles))

      createMapLoop(0, allTiles, Set.empty)

  /**
   * Implementation of [[CreateModuleType]] using randomized logic and a state monad.
   * City placement, size, and shape vary on each execution.
   */
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


  given undeterministicMap: CreateModuleType = UndeterministicMapModule

  /**
   * Factory method to create a map using a selected module.
   *
   * @param size            map size
   * @param CreateMapModule strategy module
   * @return a WorldMap instance
   */

  def createWorldMap(size: Int)(using CreateMapModule: CreateModuleType): WorldMap =
    CreateMapModule.createMap(size)

  extension (worldMap: WorldMap)

    /**
     * Calculates the size (dimension) of the map by inspecting tile coordinates.
     *
     * @return an integer representing the width/height of the square map
     */
    def getSizeOfTheMap: Int = worldMap.flatMap(_._2).foldLeft(0)((acc, xy) => math.max(acc, math.max(xy._1, xy._2))) + 1

    /**
     * Retrieves a city by its name, if it exists.
     *
     * @param name the name of the city
     * @return Option[City] containing the city if found
     */
    def getCityByName(name: String): Option[City] =
      worldMap.find(_._1.getName == name).map(_._1)

    /**
     * Counts the number of cities currently infected (AI-controlled).
     *
     * @return the number of infected cities
     */
    def numberOfCityInfected(): Int = worldMap.count(_._1.getOwner == Owner.AI)

    /**
     * Counts the number of cities.
     *
     * @return the number of cities
     */
    def numberOfCity(): Int = worldMap.size

    /**
     * Utility function to apply a generic filter over the map and return the name of the first matching city.
     *
     * @param f predicate function for filtering (City, TileSet) pairs
     * @return Option[String] of the city name if match found
     */
    def findInMap(f: (City, Set[(Int, Int)]) => Boolean): Option[String] =
      worldMap.find(f.tupled).flatMap((city, coords) => coords.headOption.map(_ => city.getName))

    /**
     * Returns all cities currently owned by the AI.
     *
     * @return a Set of infected cities
     */
    def AiCities: Set[City] = worldMap.filter(_._1.getOwner == Owner.AI).map(_._1)

    /**
     * Returns all cities currently owned by humans.
     *
     * @return a Set of human-controlled cities
     */
    def HumanCities: Set[City] = worldMap.filter(_._1.getOwner == Owner.HUMAN).map(_._1)

    /**
     * Identifies human cities that are adjacent to any infected (AI) city.
     *
     * @return a Set of human cities next to infected cities
     */
    def getAdjacentCities: Set[City] =
      if AiCities.isEmpty then HumanCities
      else worldMap.collect:
        case (city, coords)
          if city.getOwner == Owner.HUMAN &&
            coords.exists(adjacentTo(worldMap.filter(_._1.getOwner == Owner.AI).flatMap(_._2), worldMap.getSizeOfTheMap).toSet.contains)
        => city

    /**
     * Replaces an existing city on the map with a new instance, preserving its tile coordinates.
     *
     * @param city the updated city to be inserted
     * @return a new WorldMap with the updated city
     */
    def changeACityOfTheMap(city: City): WorldMap =
      worldMap.find(_._1.getName == city.getName)
        .map((_, coords) => worldMap.filterNot(_._1.getName == city.getName) + (city -> coords))
        .getOrElse(worldMap)





