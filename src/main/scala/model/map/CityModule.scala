package model.map

import model.util.Util.*

/**
 * The CityModule contains definitions and behavior for cities in the game,
 * including creation, properties, and operations such as infection and sabotage.
 */
object CityModule:

  /**
   * Represents the ownership of a city, either controlled by AI or a human player.
   */
  enum Owner:
    case AI, HUMAN

  /**
   * Abstract interface defining the behavior of a City.
   */
  trait CityInterface:

    /** Opaque type representing a City entity */
    type City

    /**
     * Creates a new city instance.
     *
     * @param name the name of the city
     * @param size the size of the city
     * @param isCapital whether the city is a capital
     * @return a new City instance
     */
    def createCity(name: String, size: Int, isCapital: Boolean): City

    extension (city: City)

      /** @return the name of the city */
      def getName: String

      /** @return the size of the city */
      def getSize: Int

      /** @return the current owner of the city */
      def getOwner: Owner

      /**
       * Computes and returns the defense level of the city.
       * Base defense is increased by 5 per size unit.
       *
       * @return the defense value
       */
      def getDefense: Int

      /** @return true if the city is a capital */
      def isCapital: Boolean

      /**
       * Infects the city, changing its ownership to AI.
       *
       * @return a new City instance with AI ownership
       */
      def infectCity(): City

      /**
       * Sabotages the city, decreasing its defense.
       *
       * @return a new City instance with reduced defense
       */
      def sabotateCity(playerAttack: Int): City


  /**
   * Implementation of the CityInterface.
   */
  object CityImpl extends CityInterface:

    /**
     * Internal representation of a City.
     *
     * @param name the name of the city
     * @param size the size of the city
     * @param owner the owner of the city
     * @param isCapital flag indicating if it is a capital city
     * @param defense the base defense value of the city
     */
    private case class CityImpl(name: String,
                                size: Int,
                                owner: Owner,
                                isCapital: Boolean,
                                defense: Int)

    opaque type City = CityImpl

    def createCity(name: String, size: Int, isCapital: Boolean): City =
      CityImpl(name, size, Owner.HUMAN, isCapital, 40)

    extension (city: City)

      /** Returns the name of the city. */
      def getName: String = city.name

      /** Returns the size of the city. */
      def getSize: Int = city.size

      /** Returns the current owner of the city (AI or HUMAN). */
      def getOwner: Owner = city.owner

      /**
       * Calculates the total defense of the city.
       * Base defense is increased by 5 per unit of city size.
       *
       * @return the computed defense value
       */
      def getDefense: Int = city.defense + (city.size * 5)

      /** Indicates whether this city is a capital. */
      def isCapital: Boolean = city.isCapital

      /**
       * Infects the city, transferring ownership to the AI.
       *
       * @return a new city instance with AI ownership
       */
      def infectCity(): City = city.copy(owner = Owner.AI)

      /**
       * Sabotages the city, reducing its defense by 20 points.
       *
       * @return a new city instance with reduced defense
       */
      def sabotateCity(playerAttack:Int): City = city.copy(defense = city.defense - playerAttack)
      

      /**
       * Defenses the city, improving its defense by 20 points.
       *
       * @return a new city instance with improved defense
       */
      def defenseCity(): City = city.copy(defense = city.defense + 20)
