package model


object MenageCity:
  enum Owner:
    case AI
    case HUMAN

  trait City:

    def getName: String
    def getOwner: Owner
    def getSize: Int
    def changeOwner(newOwner: Owner): Unit

  object City:
    def apply(name:String, size:Int) : City = BasicCity(name, Owner.HUMAN, size)


  private class BasicCity(name: String, startingOwner: Owner, size: Int) extends City:

    private var owner: Owner = startingOwner

    override def getName: String = name

    override def getSize: Int = size

    override def getOwner: Owner = owner

    override def changeOwner(newOwner: Owner): Unit =
      owner = newOwner





