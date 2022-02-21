package akkastuff

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}
import akka.pattern.{BackoffOpts, BackoffSupervisor}
import akka.util.ByteString

import java.net.InetSocketAddress
import scala.concurrent.duration.DurationInt

class SocketServiceSupervisor extends Actor with ActorLogging {

  def actorRefFactory  = context

  private val supervisor = context.actorOf({
    BackoffSupervisor.props(
      BackoffOpts.onStop(
        childProps   = Props[Server],
        childName    = "Server",
        minBackoff   = 3.seconds,
        maxBackoff   = 30.seconds,
        randomFactor = 0.2
      )
    )
  })

  override def receive = Map.empty
}

class Server extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 5555))

  def receive = {
    case b @ Bound(localAddress)      =>
      println("processing: " + b)
      context.parent ! b

    case CommandFailed(_: Bind)       => context.stop(self)

    case c @ Connected(remote, local) =>
      println("processing: " + c)
      Thread.sleep(30000)
      val handler = context.actorOf(Props[SimplisticHandler]())
      val connection = sender()
      connection ! Register(handler)
//      val data = ByteString("stuff")
//      connection ! Write(data)
  }
}

class SimplisticHandler extends Actor {
  import Tcp._
  def receive = {
    case Received(data) =>
      println("processing: " + data.utf8String)
      sender() ! Write(data)
    case PeerClosed     =>
      context.stop(self)
  }
}
