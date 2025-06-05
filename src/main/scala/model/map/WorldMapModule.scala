package model.map

import model.map.CityModule.*
import model.map.CityModule.CityImpl.*
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

  private type Coord = (Int, Int)

  opaque type WorldMap = Set[(City, Set[Coord])]

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

      def expandCity(start: Coord, available: Set[Coord], desiredSize: Int): Set[Coord] =
        @tailrec
        def expandLoop(frontier: List[Coord], visited: Set[Coord]): Set[Coord] =
          if visited.size >= desiredSize || frontier.isEmpty then visited
          else
            val next = frontier.head
            val newNeighbors = adjacentTo(Set(next),size).toSet.intersect(available).diff(visited)
            expandLoop(frontier.tail ++ newNeighbors, visited + next)

        expandLoop(List(start), Set(start)).intersect(available)

      @tailrec
      def createMapLoop(index: Int, available: Set[Coord], acc: WorldMap): WorldMap =
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

      createMapLoop(0, allCoords(size), Set.empty)

  /**
   * Implementation of [[CreateModuleType]] using randomized logic and a state monad.
   * City placement, size, and shape vary on each execution.
   */
  object UndeterministicMapModule extends CreateModuleType:

    private def placeCities(size: Int, remaining: Set[Coord], letterOffset: Int = 0, cityPlaced: WorldMap = Set.empty, remainingCapitals: Int = 5): LazyList[WorldMap] =
      if remaining.isEmpty then LazyList(cityPlaced)
      else
        for
          start <- LazyList.from(remaining)
          isCapital = remainingCapitals > 0 && doesTheActionGoesRight(50)
          tiles = growCity(start, isCapital, remaining, size)
          city = createCity(letterAt(letterOffset, isCapital), tiles.size, isCapital)
          updatedAcc = cityPlaced + (city -> tiles)
          updatedRemaining = remaining -- tiles
          result <- placeCities(
            size,
            updatedRemaining,
            letterOffset + 1, updatedAcc,
            if isCapital then remainingCapitals - 1 else remainingCapitals)

        yield result

    private def randomMaxSize(isCapital: Boolean): Int =
      3 + Random.nextInt(3) + (if isCapital then 4 else 0)

    private def growCity(start: Coord, isCapital :Boolean,available: Set[Coord], mapSize: Int): Set[Coord] =
      @tailrec
      def expand(frontier: List[Coord], built: Set[Coord]): Set[Coord] =
        if built.size >= randomMaxSize(isCapital) || frontier.isEmpty then built
        else
          val next = frontier.head
          val neighbors = adjacentTo(built,mapSize).filter(c =>
            c._1 >= 0 && c._2 >= 0 && c._1 < mapSize && c._2 < mapSize && available.contains(c) && !built.contains(c)
          )
          expand(frontier.tail ++ neighbors, built + next)

      expand(List(start), Set(start))
    override def createMap(size: Int): WorldMap =

        val fullMaps: LazyList[WorldMap] = placeCities(size, allCoords(size))

        fullMaps.find(m => m.flatMap(_._2).size == size * size ).get

  private def allCoords(size: Int) = {
    (for
      x <- 0 until size
      y <- 0 until size
    yield (x, y)).toSet
  }

  private def validNeighbors(seeds: List[Coord], size: Int, blocked: Set[Coord]): List[Coord] =
      for
        (x, y) <- seeds
        dx <- -1 to 1
        dy <- -1 to 1
        if dx != 0 || dy != 0
        nx = x + dx
        ny = y + dy
        if 0 <= nx && nx < size
        if 0 <= ny && ny < size
        if !blocked.contains((nx, ny))
      yield (nx, ny)

  private def adjacentTo(tiles: Set[Coord], size: Int): List[(Coord)] =
    validNeighbors(tiles.toList, size, tiles)


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
    def findInMap(f: (City, Set[Coord]) => Boolean): Option[String] =
      worldMap.find(f.tupled).flatMap((city, coords) => coords.headOption.map(_ => city.getName))

    /**
     * Returns all cities currently owned by the AI.
     *
     * @return a Set of infected cities
     */
    def aiCities: Set[City] = worldMap.filter(_._1.getOwner == Owner.AI).map(_._1)

    /**
     * Returns all cities currently owned by humans.
     *
     * @return a Set of human-controlled cities
     */
    def humanCities: Set[City] = worldMap.filter(_._1.getOwner == Owner.HUMAN).map(_._1)
    /**
     * Counts the number of capitals currently infected (AI-controlled).
     *
     * @return the number of infected capitals
     */
    def capitalConqueredCounter: Int = worldMap.count(map => map._1.isCapital && map._1.getOwner == Owner.AI)

    /**
     * Identifies human cities that are adjacent to any infected (AI) city.
     *
     * @return a Set of human cities next to infected cities
     */
    def getAdjacentCities: Set[City] =
      if aiCities.isEmpty then humanCities
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







