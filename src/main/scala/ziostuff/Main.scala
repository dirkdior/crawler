package ziostuff

import zio.ZIO

object Main extends App {

  val a = ZIO.succeed(40)

  val now = ZIO.effectTotal(System.currentTimeMillis())

  println(
    now
  )

}
