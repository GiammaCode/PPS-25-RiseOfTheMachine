package model.map

import model.util.Util.*

object CityModule:

  enum Owner:
    case AI, HUMAN

  trait CityInterface:

    type City
    def createCity(name: String, size: Int, isCapital: Boolean): City

    extension (city: City)
      def getName: String
      def getSize: Int
      def getOwner: Owner
      def getDefense: Int
      def isCapital: Boolean
      def infectCity(): City
      def sabotateCity(): City


  object CityImpl extends CityInterface:

    private case class CityImpl(name: String,
                                size: Int,
                                owner: Owner,
                                isCapital: Boolean,
                                defense: Int)

    opaque type City = CityImpl


    def createCity(name: String, size: Int,isCapital: Boolean): City =
      CityImpl(name, size, Owner.HUMAN, isCapital,40)

    extension (city: City)
      def getName: String = city.name
      def getSize: Int = city.size
      def getOwner: Owner = city.owner
      def getDefense: Int = city.defense + (city.size*5)

      def isCapital: Boolean = city.isCapital
      def infectCity(): City =
       city.copy(owner = Owner.AI)
      def sabotateCity(): City =
        city.copy(defense = city.defense - 20)



