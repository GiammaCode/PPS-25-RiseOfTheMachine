package model.map

import model.util.Percentage.*

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
      def tryToInfectCity(): City
      def tryToSabotateCity(poss: Int): City


  object CityImpl extends CityInterface:

    private case class CityImpl(name: String,
                                size: Int,
                                owner: Owner,
                                isCapital: Boolean, defense: Int)

    opaque type City = CityImpl

    def createCapital(name: String, size: Int): City =
      CityImpl(name, size, Owner.HUMAN, true,40)

    def createCity(name: String, size: Int): City =
      CityImpl(name, size, Owner.HUMAN, false,40)

    extension (city: City)
      def getName: String = city.name
      def getSize: Int = city.size
      def getOwner: Owner = city.owner
      def getDefense: Int = city.defense + (city.size*5)

      def isCapital: Boolean = city.isCapital
      def tryToInfectCity(): City =
        if city.owner == Owner.HUMAN && doesAttackWorks(getDefense)
        then city.copy(owner = Owner.AI)
        else city
      def tryToSabotateCity(poss: Int): City =
        if  city.owner==Owner.HUMAN && doesAttackWorks(poss)
        then city.copy(defense = city.defense - 20)
        else city



