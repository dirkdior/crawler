package random

import scala.collection.mutable.ArrayBuffer

object SESolution {
  sealed trait OrderResult
  object OutOfStock extends OrderResult
  case class Processed(delivery: Int) extends OrderResult
  object NotAvailable extends OrderResult

  private val packing           = 0.5
  private val carryToPostOffice = 0.25
  private val delivery          = 2

  private var orderNum: Int = 0

  private var stockMap: Map[String, Int] = Map("t-shirt" -> 5, "jeans" -> 3, "dress" -> 4)

  private def fulfilOrder(orderN: Int) = Math.ceil((orderN * (packing + carryToPostOffice)) + delivery).toInt
  def order(item: String): OrderResult = {
    orderNum += 1
    val orderN: Int = orderNum
    stockMap.get(item) match {
      case Some(stock) =>
        if (stock == 0)
          OutOfStock
        else {
          stockMap += (item -> (stock - 1))
          Processed(fulfilOrder(orderN))
        }
      case None        =>
        NotAvailable
    }
  }
}

object TestSESolution extends App {
//  println(SESolution.order("t-shirt"))
//  println(SESolution.order("jeans"))
//  println(SESolution.order("jeans"))
//  println(SESolution.order("jeans"))
//  println(SESolution.order("jeans"))
//  println(SESolution.order("t-shirts")) //Question Here

  val arr = Array(1, 2, 3, 4, 5)
  val brr = Array(30, 100)

  println((arr ++ brr).mkString("Array(", ", ", ")"))

  def giveNewArr(arr: Array[Int], indexToSwap: Int): Array[Int] = {
    val workingArr = ArrayBuffer[Int]()
    arr.zipWithIndex.map { case (elem, index) =>
      if (indexToSwap == index)
        workingArr += arr(indexToSwap + 1)
      else if ((indexToSwap + 1) == index)
        workingArr += arr(indexToSwap)
      else workingArr += elem
    }
    workingArr.toArray
  }

  println(giveNewArr(arr = Array(3, 4, 5, 6), indexToSwap = 1).mkString("Array(", ", ", ")"))

  println(Array(1, 2) sameElements Array(2, 1))

  println(Array(3, 1, 2).sorted.mkString("Array(", ", ", ")"))
}

object Random {
  case class Order(orderNumber: Int) extends AnyVal
  case class Days(value: Int) extends AnyVal
  // return -1 for edge cases
  def calculateDayToOrder(orders: List[Order]): Map[Order, Days] = {
    val postOfficeDelivery = 2
    val packing            = 0.5
    val toPostOffice       = 0.25

    orders.map { order =>
      val sellerTimeSpent = order.orderNumber * (packing + toPostOffice)
      val totalTime       = sellerTimeSpent + postOfficeDelivery
      (order, Days(Math.ceil(totalTime).toInt))

    }.toMap

  }

  val orders = (1 to 5).map(Order)

  println(calculateDayToOrder(orders.toList).toList.sortBy(_._2.value))

  trait OrderResult
  case class Accepted(order: String) extends OrderResult
  case class OutOfOrder(order: String) extends OrderResult
  case class NotAvailable(order: String) extends OrderResult

  def getOrderSchedule(orders: List[String]): List[OrderResult] = {
    var store = Map("t-shirt" -> 5, "jeans" -> 3, "dress" -> 4)
    orders.map { order =>
      store.get(order) match {
        case Some(value) =>
          if (value == 0) {
            OutOfOrder(order)
          }
          else {
            store = store.updated(order, value - 1)
            Accepted(order)
          }
        case None        => NotAvailable(order)
      }
    }
  }

  val ordersStr = List(
    "t-shirt",
    "jeans",
    "jeans",
    "jeans",
    "jeans",
    "t-shirt"
  )

  println(getOrderSchedule(ordersStr))
}
