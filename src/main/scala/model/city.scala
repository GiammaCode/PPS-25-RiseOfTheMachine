package model


object city:
  enum Owner:
    case AI
    case HUMAN
  trait city:

    def getName: String
    def getOwner: Owner
    def getSize: Int
    def changeOwner(newOwner: Owner): Unit





