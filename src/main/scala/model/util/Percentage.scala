package model.util

import scala.util.Random

object Percentage:
   def doesAttackWorks(percentage: Int): Boolean =
    Random.nextInt(100) > percentage

 
