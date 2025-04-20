package model


object MenageCity:
  enum Owner:
    case AI
    case HUMAN

  trait CityModule:

    type City
    def createCity(name: String, size: Int): City
    extension(city: City)
      def getName: String
      def getOwner: Owner
      def getSize: Int
      def conqueredCity(): City


  object BasicCity extends CityModule:
    case class cityImpl(name:String, size:Int, owner: Owner)

    opaque type City = cityImpl
    override def createCity(name: String, size : Int): City = cityImpl(name, size, Owner.HUMAN)
    extension (city: City)
      def getName: String = city.name
      def getSize: Int = city.size
      def getOwner: Owner = city.owner
      def conqueredCity(): City = city.copy(owner = Owner.AI)





