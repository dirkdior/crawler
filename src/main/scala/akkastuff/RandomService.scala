package akkastuff

import akka.actor._
import akkastuff.MyActor.Greeting

object RandomService extends App {

  val system  = ActorSystem("mySystem")
  val myActor = system.actorOf(Props[MyActor]())

  myActor ! Greeting("Dan")
}

object MyActor {
  case class Greeting(from: String)
  case object Goodbye
}

class MyActor extends Actor with ActorLogging {
  import MyActor._

  def myActorChild = context.actorOf(Props[MyActorChild]())

  val a = myActorChild
  val b = myActorChild

  println(a)

  println(b)

  def receive = {
    case g: Greeting =>
      log.info(s"I was greeted by ${g.from}.")
      myActorChild ! g
      a ! g
      b ! g
      Thread.sleep(3000)
      context.children foreach { child => println("A child: " + child) }
    case Goodbye           => log.info("Someone said goodbye to me.")
  }
}

class MyActorChild extends Actor with ActorLogging {
  import MyActor._

  def receive = {
    case Greeting(greeter) =>
      log.info(s"I was greeted by $greeter.")
    case Goodbye           => log.info("Someone said goodbye to me.")
  }
}