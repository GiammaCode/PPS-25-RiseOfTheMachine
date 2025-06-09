/**
 * Utility functions for probability handling and city naming.
 */
package model.util

import scala.util.Random

object Util:

  /**
   * Returns a boolean based on a given success probability.
   *
   * This simulates a probabilistic event: given a percentage (0-100),
   * it returns true with that likelihood.
   *
   * @param percentage The probability of success, from 0 to 100.
   * @return true with the given probability, false otherwise.
   */
  def doesTheActionGoesRight(percentage: Int): Boolean = {
    val maxProbability = 100
    Random.nextInt(maxProbability) < percentage
  }

  /**
   * Converts a number to a corresponding letter string,
   * using capital or lowercase depending on the flag.
   *
   * This is used to generate city names based on index and type (capital/non-capital).
   *
   * @param n the index of the letter (e.g., 0 -> 'A' or 'a')
   * @param isCapital true if the letter should be uppercase (capital city), false otherwise
   * @return a string representing the corresponding letter
   */
  def letterAt(n: Int, isCapital: Boolean): String =
    if isCapital then {
      val firstCapitalLetterAscii = 65 // 'A'
      (firstCapitalLetterAscii + n).toChar.toString
    } else {
      val firstLowercaseLetterAscii = 97 // 'a'
      (firstLowercaseLetterAscii + n).toChar.toString
    }
