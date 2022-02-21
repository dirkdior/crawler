package db.kafka

import akka.actor.typed.scaladsl.{ AbstractBehavior, ActorContext, Behaviors }
import akka.actor.typed.{ ActorSystem, Behavior, PostStop, PreRestart, SupervisorStrategy }

object Main extends App {
  implicit val system: ActorSystem[KafkaServiceProtocol.Command] = ActorSystem(
    startSystem,
    "KafkaConnectionSystem"
  )

  def startSystem: Behavior[KafkaServiceProtocol.Command] = {
    Behaviors
      .supervise[KafkaServiceProtocol.Command] {
        Behaviors.setup { context =>
          println("\n\nStarting KafkaConnection ActorSystem...")
          val consumer = new KafkaConsumerService(topicName = "Session")(context)
          Behaviors
            .receiveMessage[KafkaServiceProtocol.Command] { msg =>
              println(s"processing: $msg")
              throw new Exception("Something went wrong")
              Behaviors.same
            }
            .receiveSignal {
              case (_, signal) if signal == PreRestart || signal == PostStop =>
                println("Received Signal: " + signal)
                println("Stopping Kafka Consumer")
                consumer.shutdownConsumer()
                Behaviors.same
            }
        }
      }
      .onFailure[Exception](SupervisorStrategy.restart)
  }

  val kafkaProducerService = new KafkaProducerService(topicName = "Session")
  kafkaProducerService.send(
    KafkaMsg(
      code  = "*123#",
      stuff = "something fancy"
    )
  )

  kafkaProducerService.send(
    KafkaMsg(
      code  = "*123#",
      stuff = "something fancy"
    )
  )

  kafkaProducerService.send(
    KafkaMsg(
      code  = "*123#",
      stuff = "something fancy"
    )
  )

  kafkaProducerService.send(
    KafkaMsg(
      code  = "*123#",
      stuff = "something fancy"
    )
  )

  Thread.sleep(2000)

  system ! KafkaServiceProtocol.RandomCommand

  Thread.sleep(2000)
}
