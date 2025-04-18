package model

import model.city.*

class basicCity(name:String, startingOwner: Owner, size: Int) extends city:

   private var owner: Owner = startingOwner

   override def getName: String = name

   override def getSize: Int = size

   override def getOwner: Owner = owner

   override def changeOwner(newOwner: Owner): Unit =
      owner = newOwner




