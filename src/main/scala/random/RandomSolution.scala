package random

import scala.collection.mutable.ListBuffer

object RandomSolution extends App {

  //discuss pattern
  //start returnNewRow function, takes array, returns array
  //case for array one item
  //add to array 'resArray :+ 1'

  val rows = 20

  var solutionArray: Array[Int] = Array.emptyIntArray

  (1 to rows).foreach { _ =>
    solutionArray = returnNewRow(solutionArray)
    solutionArray.foreach(i => print(i + " "))
    println()
  }

  def returnNewRow(arr: Array[Int]): Array[Int] = {
    val len = arr.length
    var resArray: Array[Int] = Array.emptyIntArray
    if(arr.isEmpty)
      Array[Int](1)
    else if(len == 1)
      Array[Int](1, 1)
    else {
      arr.zipWithIndex.foreach {
        case (num, index) =>
          lazy val thisRes = num + arr(index + 1)

          if(index == 0)
            resArray = Array(1, thisRes)
          else if(index == len - 1)
            resArray = resArray :+ 1
          else
            resArray = resArray :+ thisRes
      }
      resArray
    }
  }
}

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1: Pascal's Triangle
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2: Parentheses Balancing
   */
  def balance(chars: List[Char]): Boolean = {
    def f(chars: List[Char], numOpens: Int): Boolean = {
      if (chars.isEmpty) {
        numOpens == 0
      } else {
        val h = chars.head
        val n =
          if (h == '(') numOpens + 1
          else if (h == ')') numOpens - 1
          else numOpens
        if (n >= 0) f(chars.tail, n)
        else false
      }
    }

    f(chars, 0)
  }

  /**
   * Exercise 3: Counting Change
   * Write a recursive function that counts how many different ways you can make
   * change for an amount, given a list of coin denominations. For example,
   * there are 3 ways to give change for 4 if you have coins with denomiation
   * 1 and 2: 1+1+1+1, 1+1+2, 2+2.
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    def f(lastMaxCoin_total_coll: List[(Int, Int)], count: Int): Int = {
      if (lastMaxCoin_total_coll.isEmpty) {
        count
      } else {
        val b = ListBuffer[(Int, Int)]()
        var newCount = count
        for ((lastMaxCoin, total) <- lastMaxCoin_total_coll) {
          if (total < money) {
            for (c <- coins) {
              if (c >= lastMaxCoin) {
                val e = (c, total + c)
                b += e
              }
            }
          } else if (total == money) {
            newCount += 1
          }
        }

        f(b.toList, newCount)
      }
    }

    val b = coins.map { c => (c, c) }
    f(b, 0)
  }
}

