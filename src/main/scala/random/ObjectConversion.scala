package random

import scala.concurrent.duration.{Duration, FiniteDuration}

object ObjectConversion extends App {
  val stuff: Stuff = Stuff("dayo", 1, "hero")

  val stuffStr: String = stuff.toString

//  val stuffConverted: Stuff = stuffStr.to

  val duration = Duration("3 seconds")
  val fd1: Option[FiniteDuration] = Some(duration).collect { case d: FiniteDuration => d }
  val fd2: Option[FiniteDuration] = None

  val fd1String: Option[String]   = fd1.map(_.toString)
  val fd2String: Option[String]   = fd2.map(_.toString)

  println(fd1)
  println(fd1String)

  println("\n")

  println(fd2)
  println(fd2String)
}

case class Stuff(
  name: String,
  id: Int,
  other: String
)
