package random

import scala.util.Random

object TestInheritance extends TestInheritanceT

private [random] trait TestInheritanceT {
  def printSmt():Unit = println("something oo")

  private val obj = Random.nextInt
  def getObj: Int = obj
}


object TestObjects extends TestInheritanceT

object RunThis extends App {
  println(TestInheritance.getObj)
  println(TestInheritance.getObj)
  println(TestInheritance.getObj)
  println(TestObjects.getObj)
}
