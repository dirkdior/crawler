package random

object NetworkCode extends Enumeration {
  val AirtelZambia       = Value(64501)
  val MTNZambia          = Value(64502)
  val Zamtel             = Value(64503)
  val TNMMalawi          = Value(65001)
  val AirtelMalawi       = Value(65010)
  val VodacomSouthAfrica = Value(65501)
  val TelkomSouthAfrica  = Value(65502)
  val CellCSouthAfrica   = Value(65507)
  val MTNSouthAfrica     = Value(65510)
  val Athena             = Value(99999)
}

object TestingEnums extends App {
//  val testNC = NetworkCode.MTNZambia
//  println(testNC)
//  println(testNC.id)
//  println(testNC.outerEnum)

  val inputStr         = "*357*1*"
  val inputParam       = inputStr.drop(1).stripSuffix("#")
  val idx = inputParam.indexOf("*") match {
    case -1 => inputStr.length
    case x => x
  }
  val input            = inputParam.drop(idx + 1)

  println(input)
}
