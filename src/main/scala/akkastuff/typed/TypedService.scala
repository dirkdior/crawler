package akkastuff.typed

import akka.{ Done => AkkaDone }
import akka.actor.CoordinatedShutdown
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ ActorRef, ActorSystem, Behavior, Props, SpawnProtocol }
import akka.actor.typed.scaladsl.{ ActorContext, Behaviors }
import akka.util.Timeout
import akkastuff.typed.CashOutServiceRegistry.timeout
import akkastuff.typed.TypedService.{ Event, InitiateCashOut }
import akkastuff.typed.TypedServiceManager.{ GetHandlerRequest, GetHandlerResponse, ManagerEvent }

import java.util.concurrent.ConcurrentHashMap
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ ExecutionContext, Future }
import scala.util._

object TypedService {

  sealed trait Event
  final case class InitiateCashOut(payload: String) extends Event

  sealed trait Data
  case object Empty extends Data
  case object Done extends Data
  final case class CashOutData(
    customer: String,
    amount: Double,
    current_latitude: Double,
    current_longitude: Double,
    preferred_distance: Int
  ) extends Data

  private var serviceId: String = ""

  def apply(str: String): Behavior[Event] = Behaviors.setup { context =>
    import scala.concurrent.ExecutionContext.Implicits.global
    serviceId = str
    CoordinatedShutdown(context.system)
      .addTask(CoordinatedShutdown.PhaseBeforeServiceUnbind, "InteractionHandler ShutDown Task") { () =>
        //        implicit val timeout: Timeout = Timeout(5.seconds)
        val randomFut: Future[AkkaDone] = Future {
          println("Shutting down TypedService, ID is [{}]...", serviceId)
          AkkaDone
        }
        randomFut
      }

    initiate(Empty, str, context)
  }

  private def initiate(data: Data, str: String, context: ActorContext[Event]): Behavior[Event] = Behaviors.receiveMessage[Event] {
    msg =>
      (msg, data) match {
        case (a @ InitiateCashOut(_), Empty) =>
          //continue lines of code
          println("While Initiating, processing: " + a)
          println(s"Service Id: $serviceId. Context Name: ${context.self.path.name}")
          initiate(Done, str, context)
        case (a @ InitiateCashOut(_), Done)  =>
          //continue lines of code
          println("While Initiating, Its now Done...")
          done(Done)
        case _                               =>
          println("While Unhandled, processing: ")
          Behaviors.unhandled
      }
  }

  private def done(data: Data): Behavior[Event] = {
    data match {
      case Done =>
        println("Hey! I'm done and saving the data!")
      case _    =>
        println("Ignoring...")
    }
    Behaviors.receiveMessage[Event] { msg =>
      (msg, data) match {
        case _ =>
          println("Received a request when DONE")
          Behaviors.unhandled
      }
    }
  }

//  private def test(data: Data): Behavior[Event] = Behaviors.withTimers[Event] { timers =>
//    timers.startSingleTimer(InitiateCashOut("s"), 1.seconds)
//  }
}

class TypedServiceC {
  private var serviceId: String           = ""
  import TypedService._
  def apply(str: String): Behavior[Event] = Behaviors.setup { context =>
    import scala.concurrent.ExecutionContext.Implicits.global
    serviceId = str
    CoordinatedShutdown(context.system)
      .addTask(CoordinatedShutdown.PhaseBeforeServiceUnbind, "InteractionHandler ShutDown Task") { () =>
        //        implicit val timeout: Timeout = Timeout(5.seconds)
        val randomFut: Future[AkkaDone] = Future {
          println("Shutting down TypedService, ID is [{}]...", serviceId)
          AkkaDone
        }
        randomFut
      }
    initiate(Empty, str, context)
  }

  private def initiate(data: Data, str: String, context: ActorContext[Event]): Behavior[Event] = Behaviors.receiveMessage[Event] {
    msg =>
      (msg, data) match {
        case (a @ InitiateCashOut(_), Empty) =>
          //continue lines of code
          println("While Initiating, processing: " + a)
          println(s"Service Id: $serviceId. Context Name: ${context.self.path.name}")
          initiate(Done, str, context)
        case (a @ InitiateCashOut(_), Done)  =>
          //continue lines of code
          println("While Initiating, Its now Done...")
          done(Done)
        case _                               =>
          println("While Unhandled, processing: ")
          Behaviors.unhandled
      }
  }

  private def done(data: Data): Behavior[Event] = {
    data match {
      case Done =>
        println("Hey! I'm done and saving the data!")
      case _    =>
        println("Ignoring...")
    }
    Behaviors.receiveMessage[Event] { msg =>
      (msg, data) match {
        case _ =>
          println("Received a request when DONE")
          Behaviors.unhandled
      }
    }
  }
}

object TypedServiceManager {
  sealed trait ManagerEvent
  final case class GetHandlerRequest(customerAuthId: String, replyTo: ActorRef[GetHandlerResponse]) extends ManagerEvent
  final case class GetHandlerResponse(handler: ActorRef[TypedService.Event]) extends ManagerEvent
  def apply(): Behavior[ManagerEvent] =
    Behaviors.setup { ctx =>
      println("Starting TypedServiceManager...")
//      ctx.log.info("Starting TypedServiceManager...")
      Behaviors.receiveMessage { case g: GetHandlerRequest =>
        val handler = ctx.child(g.customerAuthId) match {
          case Some(value) =>
            value.asInstanceOf[ActorRef[TypedService.Event]]
          case None        =>
//            val handler = ctx.spawn(new TypedServiceC().apply(g.customerAuthId), g.customerAuthId)
            val handler = ctx.spawn(TypedService.apply(g.customerAuthId), g.customerAuthId)
            handler
        }
        g.replyTo ! GetHandlerResponse(handler)
        Behaviors.same
      }
    }
}

object NewService extends App {
  implicit val system: ActorSystem[SpawnProtocol.Command] = ActorSystem(HelloWorldMain(), "helloSystem")
  implicit val executionContext: ExecutionContext         = system.executionContext

  val fut = for {
    serviceManager <- system.ask[ActorRef[ManagerEvent]](
      SpawnProtocol.Spawn(TypedServiceManager(), "TypedServiceManager", props = Props.empty, _)
    )
    handlerInfo1 <- serviceManager.ask[GetHandlerResponse](ref => GetHandlerRequest("111", ref))
    handlerInfo2 <- serviceManager.ask[GetHandlerResponse](ref => GetHandlerRequest("222", ref))
    handlerInfo3 <- serviceManager.ask[GetHandlerResponse](ref => GetHandlerRequest("333", ref))
  } yield (handlerInfo1.handler, handlerInfo2.handler, handlerInfo3.handler)

  fut onComplete {
    case Success(handlerTup) =>
      println("Now reaching handler 1..." + handlerTup._1)
//      system.log.info("Now reaching handler... {}", handler)
      handlerTup._1 ! InitiateCashOut(
        payload = "Stuff"
      )

      println("Now reaching handler 2..." + handlerTup._2)
      //      system.log.info("Now reaching handler... {}", handler)
      handlerTup._2 ! InitiateCashOut(
        payload = "Stuff"
      )

      println("Now reaching handler 3..." + handlerTup._3)
      //      system.log.info("Now reaching handler... {}", handler)
      handlerTup._3 ! InitiateCashOut(
        payload = "Stuff"
      )
    case Failure(ex)         =>
      println("Something went wrong! " + ex)
//      system.log.error("Something went wrong! {}", ex)
  }
}

object RunService extends App {
  implicit val system: ActorSystem[SpawnProtocol.Command] = ActorSystem(HelloWorldMain(), "helloSystem")
  implicit val executionContext: ExecutionContext         = system.executionContext
  val actor1                                              =
    system.ask[ActorRef[TypedService.Event]](SpawnProtocol.Spawn(TypedService("abc"), "TypedService", props = Props.empty, _))
  val actor2                                              =
    system.ask[ActorRef[TypedService.Event]](SpawnProtocol.Spawn(TypedService("efg"), "TypedService", props = Props.empty, _))
  val actor3                                              =
    system.ask[ActorRef[TypedService.Event]](SpawnProtocol.Spawn(TypedService("hij"), "TypedService", props = Props.empty, _))

  val actorsFut                                           = for {
    a <- actor1
    b <- actor2
    c <- actor3
  } yield (a, b, c)

  actorsFut onComplete {
    case Success(value) =>
      println(value)
      value._1 ! InitiateCashOut(
        payload = "Stuff"
      )
      Thread.sleep(1000)
      value._1 ! InitiateCashOut(
        payload = "ProcessIt"
      )
      Thread.sleep(1000)
      value._1 ! InitiateCashOut(
        payload = "StartAgain"
      )
    case Failure(ex)    => println(ex)
  }

}

object HelloWorldMain {
  def apply(): Behavior[SpawnProtocol.Command] =
    Behaviors.setup { context =>
      // Start initial tasks
      // context.spawn(...)

      SpawnProtocol()
    }
}

object CashOutServiceRegistry {
  type customerAuthId = Int
  private val handlerMap = new ConcurrentHashMap[customerAuthId, ActorRef[TypedService.Event]]

  implicit val timeout: Timeout = Timeout(3.seconds)

//  def getHandler(authId: Int)(implicit system: ActorSystem[SpawnProtocol.Command]): Future[ActorRef[TypedService.Event]] = handlerMap.containsKey(authId) match {
//    case false =>
//      implicit val executionContext: ExecutionContext = system.executionContext
////      system.ask(SpawnProtocol.Spawn(TypedService(), "", props = Props.empty, _))
//    //create the actor, then put its ref into the cache, then return the ref
//
//    case true  =>
//    //just return the actor
//  }

  def removeHandler(authId: Int): Unit = handlerMap.remove(authId)

  private def createCashOutActor()(implicit system: ActorSystem[SpawnProtocol.Command]): Future[ActorRef[TypedService.Event]] =
    system.ask(SpawnProtocol.Spawn(TypedService("lmn"), "", props = Props.empty, _))
}
