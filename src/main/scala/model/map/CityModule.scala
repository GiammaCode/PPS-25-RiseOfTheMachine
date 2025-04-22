package model.map

object CityModule:

  enum Owner:
    case AI, HUMAN

  trait CityInterface:

    type City
    def createCapital(name: String, size: Int): City
    def createCity(name: String, size: Int): City

    extension (city: City)
      def getName: String
      def getSize: Int
      def getOwner: Owner
      def getDefense: Int
      def isCapital: Boolean
      def conqueredCity(): City

  object CityImpl extends CityInterface:

    private case class CityImpl(name: String, size: Int, owner: Owner, isCapital: Boolean)

    opaque type City = CityImpl

    def createCapital(name: String, size: Int): City =
      CityImpl(name, size, Owner.HUMAN, true)

    def createCity(name: String, size: Int): City =
      CityImpl(name, size, Owner.HUMAN, false)

    extension (city: City)
      def getName: String = city.name
      def getSize: Int = city.size
      def getOwner: Owner = city.owner
      def getDefense: Int = 40 + (city.size*5)
      def isCapital: Boolean = city.isCapital
      def conqueredCity(): City = city.copy(owner = Owner.AI)
