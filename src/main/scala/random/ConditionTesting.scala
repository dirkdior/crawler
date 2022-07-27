package random

import scala.util.Try

object ConditionTesting extends App {
  sealed trait RandomT
  object RandomObject1 extends RandomT
  object RandomObject2 extends RandomT
  object RandomObject3 extends RandomT

  def supplyRandom(random: RandomT): Unit = random match {
//    case RandomObject1 | r2 @ RandomObject2 => //This absolutely does not work
//      println("It was Random 1 or 2 supplied")
//      if (r2 != null)
//        println("Well I can guarantee that Random 2 was supplied")

    case RandomObject3 =>
      println("It was Random 3 supplied")
  }

  supplyRandom(RandomObject1)
}

object ConstructStuff extends App {
  case class UssdPushServiceCode(
    shortCode: Int,
    channel: Option[String]
  ) {
    override def toString = {
      channel match {
        case Some(x) => s"*$shortCode*$x#"
        case None    => s"*$shortCode#"
      }
    }
  }

  println(constructServiceCode("*999#"))
  println(constructServiceCode("*123*25#"))
  println(constructServiceCode("456#"))
  println(constructServiceCode("*789*22"))
  println(constructServiceCode("923"))

  val toRemove = "*#".toSet
  val words    = "*123*34*44#".filterNot(toRemove)

  println(Try(words.toInt).toOption)

  private def constructServiceCode(serviceCodeStr: String): UssdPushServiceCode = {
    val firstChar: String = serviceCodeStr.take(1)
    val lastChar: String  = serviceCodeStr.takeRight(1)

    val inputParam = (firstChar, lastChar) match {
      case ("*", "#") =>
        serviceCodeStr.stripSuffix("#").drop(1)
      case (_, "#")   =>
        serviceCodeStr.stripSuffix("#")
      case ("*", _)   =>
        serviceCodeStr.drop(1)
      case _          =>
        serviceCodeStr
    }
    val idx        = inputParam.indexOf("*") match {
      case -1 => serviceCodeStr.length
      case x  => x
    }
    UssdPushServiceCode(
      shortCode = inputParam.slice(0, idx).toInt,
      channel = inputParam.drop(idx + 1) match {
        case "" => None
        case _  => Some(inputParam.drop(idx + 1))
      }
    )
  }
}
