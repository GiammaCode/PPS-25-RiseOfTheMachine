package model.util

import scala.util.Random

object Util:
  def doesAttackWorks(percentage: Int): Boolean =
    Random.nextInt(100) > percentage

  def letterAt(n: Int): String =
    (65 + n).toChar.toString

    //97 for minus


