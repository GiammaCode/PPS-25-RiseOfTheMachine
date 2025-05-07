package model.util

import scala.util.Random

object Util:
  def doesTheActionGoesRight(percentage: Int): Boolean =
    Random.nextInt(100) < percentage

  def letterAt(n: Int, isCapital: Boolean): String =
    if isCapital
    then (65 + n).toChar.toString
    else (97 + n).toChar.toString

  def calculatePercentageOfSuccess: Int =
    val percentage = 50
    percentage
