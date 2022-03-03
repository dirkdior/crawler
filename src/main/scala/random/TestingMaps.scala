package random

object TestingMaps extends App {

  var x = Map(1 -> "Alabama")
  x += (2 -> "Alaska")
  println(x)
  x += (2 -> "Las")
  println(x)

  val b = Map(1 -> "Alabama")
  b.updated(2, "Alaska")
  println(b)

  def returnNewMap(m: Map[Int, String]): Map[Int, String] = {
    var nm: Map[Int, String] = m
    nm += (3 -> "London")
    nm += (4 -> "Lagos")
    m.updated(6, "London")
    m.updated(5, "Lagos")
    nm
  }

  println(
    returnNewMap(b)
  )

  trait Numbers
  object One extends Numbers
  object Two extends Numbers
  object Three extends Numbers
  object Four extends Numbers

  def matchOrNah(t1: Numbers, t2: Numbers): Unit = (t1, t2) match {
    case (One, Two | Three) =>
      println("matched")
    case _                  =>
      println("didn't match")
  }

  matchOrNah(
    t1 = One,
    t2 = Two
  )
}
