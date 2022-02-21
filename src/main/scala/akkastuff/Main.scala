package akkastuff

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import akka.actor.{ActorRefFactory, ActorSystem, Props}
import akka.http.scaladsl.Http

import scala.concurrent.Future

object Stuff {
  sealed trait Event
  case object Stat1 extends Event
  case object Stat2 extends Event
}

object Main extends App {

  def start() = {
    println("Starting Mock Socket Service")

    implicit val system: ActorSystem = ActorSystem("MockAkkaHttpServer")

    //system.actorOf(Props[SocketServiceSupervisor], "mock-socket-service")

    val arr: Array[Int] = Array(11, 32, 44, 65)

    def futTest(a: Array[Int]): Future[Int] = Future {
      a(7)
    }

    def fmFutTest: Future[Int] = futTest(arr) flatMap {
      a: Int => Future.successful(a)
    }

    fmFutTest onComplete {
      case Success(value) =>
        println(value.toString)
      case Failure(ex)    =>
        println("Sorry, there was an error: " + ex)
    }
  }

  start()
}
