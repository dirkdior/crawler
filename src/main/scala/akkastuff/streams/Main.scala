package akkastuff.streams

import akka.NotUsed
import akka.stream.scaladsl.Source

object Main extends App {

  def countDown(from: Int): Source[Int, NotUsed] =
    Source.unfold(from) { current =>
      if (current == 0) {
        println(current)
        None
      }
      else {
        println(current)
        Some((current - 1, current))
      }
    }

  println(countDown(2))
  println(countDown(3))
  println(countDown(4))
}
