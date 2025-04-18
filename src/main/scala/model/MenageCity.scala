package model


object MenageCity:
  enum Owner:
    case AI
    case HUMAN

  trait City:

    def getName: String
    def getOwner: Owner
    def getSize: Int
    def conqueredCity(): Unit

  object City:
    def apply(name:String, size:Int) : City = new BasicCity(name, size)


  private class BasicCity(name: String, size: Int) extends City:

    private var owner: Owner = Owner.HUMAN

    override def getName: String = name

    override def getSize: Int = size

    override def getOwner: Owner = owner

    override def conqueredCity(): Unit =
      owner = Owner.AI





