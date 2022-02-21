package random

case class UssdServiceCodeDbEntry (
  networkCode: Option[Int],
  serviceCodeId: Int,
  shortCode: Option[Int],
  billingMode: Int
)

object PartitioningLists extends App {

  val serviceCodeList: List[UssdServiceCodeDbEntry] = List(
    UssdServiceCodeDbEntry(None, 1, None, 2),
    UssdServiceCodeDbEntry(Some(1), 2, None, 2),
    UssdServiceCodeDbEntry(None, 3, None, 2),
    UssdServiceCodeDbEntry(Some(1), 4, None, 2),
    UssdServiceCodeDbEntry(None, 5, None, 2),
    UssdServiceCodeDbEntry(Some(1), 6, Some(347), 2),
    UssdServiceCodeDbEntry(Some(1), 7, None, 2),
    UssdServiceCodeDbEntry(None, 8, None, 2),
    UssdServiceCodeDbEntry(None, 9, None, 2),
    UssdServiceCodeDbEntry(Some(1), 10, None, 2),
    UssdServiceCodeDbEntry(None, 11, Some(34462), 2),
    UssdServiceCodeDbEntry(Some(1), 12, None, 2),
    UssdServiceCodeDbEntry(None, 13, None, 2),
    UssdServiceCodeDbEntry(Some(1), 14, Some(34465), 2)
  )

  val (validCodes, invalidCodes) = serviceCodeList.partition(c => c.networkCode.isDefined && c.shortCode.isDefined)
  validCodes.foreach(println)
  invalidCodes.foreach(println)

  if ( invalidCodes.nonEmpty ){
    println("We have invalid USSD network codes in the database: " + invalidCodes)
  }

  val x = List(15, 10, 5, 8, 20, 12)
  println(
    x.partition(a => a > 6 && a > 10)
  )

}
