package db.mysql

import _shared.GeneratorUtils
import akka.Done

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

object Main extends App {

  //  (1 to 10) map { _ =>
  //    SessionDetailsMapper.create(
  //      SessionDetailsRow(
  //        session_id =
  //      )
  //    )
  //  }

  def func(s: Done): String = ???
  val countFrom             = 1000

  @scala.annotation.tailrec
  def loopFutures(future: Future[Done], n: Int): Done = n match {
    case 0 => Done
    case _ =>
      loopFutures(
        future.flatMap { d =>
          println(s"$d: ${(countFrom - n + 1).abs}")
          val session_id = GeneratorUtils.generateRandomId
          SessionDetailsMapper.create(
            SessionDetailsRow(
              session_id     = session_id,
              user_id        = 22990,
              insertion_day  = GeneratorUtils.generateSixDigitNum,
              service_code   = "*999#",
              phone_number   = "+2349033565644",
              duration_in_ms = 120000,
              price          = "NGN 16.0000",
              status         = "Incomplete",
              network_code   = "MTNNigeria",
              text           = "8*6*3*1*2*3*1*2*3*2*3*3"
            )
          )
        },
        n - 1
      )
  }

  loopFutures(
    Future {
      println("Starting...")
      Done
    },
    countFrom
  )

//  val session_id = GeneratorUtils.generateRandomId
//  SessionDetailsMapper.create(
//    SessionDetailsRow(
//      session_id     = session_id,
//      user_id        = 123,
//      insertion_day  = GeneratorUtils.generateSixDigitNum,
//      service_code   = "*999#",
//      phone_number   = "+2349033565644",
//      duration_in_ms = 120000,
//      price          = "NGN 16.0000",
//      status         = "Incomplete",
//      network_code   = "MTNNigeria",
//      text           = "8*6*3*1*2*3*1*2*3*2*3*3"
//    )
//  ) onComplete {
//    case Success(value)     => println(value)
//    case Failure(exception) => println(s"Failure: $exception")
//  }

  Thread.sleep(100000000)

}
