package db.kafka

import akka.{ Done, NotUsed }
import akka.actor.ActorRef
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.{ ActorSystem, SpawnProtocol }
import akka.kafka.{ AutoSubscription, ConsumerMessage, ConsumerSettings, KafkaConsumerActor, Subscriptions }
import akka.kafka.scaladsl.{ Committer, Consumer }
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream.scaladsl.{ Flow, Keep, Sink }
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import akka.actor.typed.scaladsl.adapter._
import io.circe.generic.semiauto.deriveDecoder
import io.circe.{ Decoder, parser }
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition

import scala.util.{ Failure, Success }

class KafkaConsumerService(topicName: String)(
  context: ActorContext[KafkaServiceProtocol.Command]
) {
  println("Starting KafkaConsumerService...")
  private implicit val system: ActorSystem[KafkaServiceProtocol.Command] =
    context.system.asInstanceOf[ActorSystem[KafkaServiceProtocol.Command]]
  private implicit val kafkaMsgDecoder: Decoder[KafkaMsg]                = deriveDecoder

  private val bootstrapServers                                   = "localhost:9092"
  private val consumerSettings: ConsumerSettings[String, String] =
    ConsumerSettings(system, new StringDeserializer, new StringDeserializer).withBootstrapServers(bootstrapServers)
  private val subscription: AutoSubscription                     = Subscriptions.topics(topicName)

  private val consumer: ActorRef = context.actorOf(KafkaConsumerActor.props(consumerSettings), "kafka-consumer-actor")

  private val (controlPartition, result) = Consumer
    .plainExternalSource[String, String](
      consumer,
      Subscriptions.assignment(new TopicPartition(topicName, 0))
    )
    .via(businessFlow)
    .toMat(Sink.seq)(Keep.both)
    .run()

  private def businessFlow: Flow[ConsumerRecord[String, String], KafkaMsg, NotUsed] =
    Flow[ConsumerRecord[String, String]].map { record =>
      val payload   = record.value()
      val key       = record.key()
      val partition = record.partition()
      println(s"[KafkaConsumerService]: payload $payload  Key: $key  partition $partition")
      val kafkaMsg  = parser.decode[KafkaMsg](payload).toOption.get
      kafkaMsg
    }

  println(s"Partition: $controlPartition")

  result onComplete {
    case Success(value) =>
      println("[KafkaConsumerService] Result from Consumer is: " + value)
    case Failure(ex)    =>
      println("[KafkaConsumerService] Ran into an error: " + ex.printStackTrace)
  }

  def shutdownConsumer(): Unit = {
    controlPartition.shutdown()
    consumer ! KafkaConsumerActor.Stop
  }

//  val convertFromKafkaMessage
//    : Flow[ConsumerMessage.CommittableMessage[String, Array[Byte]], (KafkaMsg, ConsumerMessage.Committable), NotUsed] =
//    Flow[ConsumerMessage.CommittableMessage[String, Array[Byte]]].map { message =>
//      val payload    = new String(message.record.value())
//      val key        = message.record.key()
//      val partition  = message.committableOffset.partitionOffset._1.partition
//      system.log.info(s"store data payload ${payload}  Key: ${key}  partition ${partition}")
//      val saleRecord = parser.decode[KafkaMsg](payload).toOption.get
//      (saleRecord, message.committableOffset)
//    }
//
//  Consumer
//    .committablePartitionedSource(consumerSettings, Subscriptions.topics(topicName))
//    .mapAsyncUnordered(1) { case (topicPartitions, source) =>
//      system.log.info(s"partition-[${topicPartitions.partition}] topic-[${topicPartitions.topic}\n\n\n")
//      source
//        .via(convertFromKafkaMessageToDailySalesFlow)
//        .map(msgCommitter => msgCommitter._2)
//        .run()
//    }
//    .toMat(Sink.ignore)(DrainingControl.apply)

  //  val consumer           = Consumer.plainSource(consumerDefaults.withGroupId(group), subscription)
  //  val consumer: ActorRef = context.actorOf(KafkaConsumerActor.props(consumerSettings))

  //  val control: DrainingControl[immutable.Seq[Done]] =
  //    Consumer
  //      .atMostOnceSource(consumerSettings, Subscriptions.topics(topicName))
  //      .mapAsync(1)(record => business(record.key, record.value()))
  //      .toMat(Sink.seq)(DrainingControl.apply)
  //      .run()
  //
  //  def business(key: String, value: String): Future[Done] = Future {
  //    println(s"Here is the message key [$key], then the value [$value]")
  //    Done
  //  }
//
//  Consumer
//    .committablePartitionedSource(consumerSettings, Subscriptions.topics(topicName))
//    .mapAsyncUnordered(1) { case (topicPartitions, source) =>
//      println(s"partition-[${topicPartitions.partition}] topic-[${topicPartitions.topic}\n\n\n")
//      source
//        .via(convertFromKafkaMessageToDailySalesFlow)
//    }

}
