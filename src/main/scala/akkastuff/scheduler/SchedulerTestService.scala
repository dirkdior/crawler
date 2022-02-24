package akkastuff.scheduler

import akka.actor.typed.{ ActorSystem, Behavior, SpawnProtocol }
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout

import scala.concurrent.duration._
import scala.language.postfixOps

object SchedulerTestService extends App {
  implicit val system: ActorSystem[SpawnProtocol.Command] = ActorSystem(
    startSystem,
    "SchedulerTestServiceSystem"
  )

  def startSystem: Behavior[SpawnProtocol.Command] = Behaviors.setup { context =>
    println("Starting SchedulerTest Service ActorSystem")
    context.spawn(TickActor(), "TickActor")
    Behaviors.same
  }

}

object TickActor {
  type Tick = String
  val tick: Tick    = "tick"
  val tok: Tick     = "tok"
  val timeout: Tick = "timeout"

  def apply(): Behavior[Tick] = Behaviors.withTimers { timers =>
    println("Starting TickActor...")
    timers.startTimerWithFixedDelay(tick, 2 seconds)
    timers.startTimerWithFixedDelay(tok, 5 seconds)
    Behaviors.receiveMessagePartial {
      case timeout =>
        println("Timeout received: " + timeout)
        Behaviors.same
      case tick    =>
        println("Tick request: " + tick)
        Behaviors.same
      case tok     =>
        println("Tok request: " + tok)
        Behaviors.same
    }
  }
}
