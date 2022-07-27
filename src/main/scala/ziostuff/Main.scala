package ziostuff

import zio._

import scala.concurrent.Future
import scala.io.StdIn

object Main extends App {

  val goShopping: UIO[Unit] = UIO.effectTotal(println("Going to the grocery store"))

  val helloWorld =
    ZIO.effect(print("Hello, ")) *> ZIO.effect(print("World!\n"))

  val flatMapVersion = ZIO.effect(print("Hello, ")).flatMap(_ => ZIO.effect(print("World!\n")))
  val forVersion     = for {
    _ <- ZIO.effect(print("Hello, "))
    _ <- ZIO.effect(print("World!\n"))
  } yield ()

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    goShopping.exitCode

//  def getUserByIdAsync(id: Int) = ZIO.effect(Some("Steve"))
//
//  def getUserById(id: Int): ZIO[Any, None.type, String] =
//    ZIO.effectAsync { callback =>
//      getUserByIdAsync(id) {
//        case Some(name) => callback(ZIO.succeed(name))
//        case None       => callback(ZIO.fail(None))
//      }
//    }
}

//object First extends App {
//  val readLine                = ZIO.effect(StdIn.readLine())
//  def printLine(line: String) = ZIO.effect(println(line))
//
//  val echo = readLine.flatMap(line => printLine(line))
//}
