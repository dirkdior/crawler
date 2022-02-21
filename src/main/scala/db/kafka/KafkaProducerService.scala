package db.kafka

import akka.actor.typed.{ ActorSystem, SpawnProtocol }
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import io.circe.generic.JsonCodec
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

case class KafkaMsg(
  code: String,
  stuff: String
)

class KafkaProducerService(topicName: String)(implicit system: ActorSystem[KafkaServiceProtocol.Command]) {
  val bootstrapServers = "localhost:9092"

  val producerSettings: ProducerSettings[String, String] =
    ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(bootstrapServers)

  // create a producer
  val kafkaProducer                                          = producerSettings.createKafkaProducer()
  val settingsWithProducer: ProducerSettings[String, String] = producerSettings.withProducer(kafkaProducer)

  def send(msg: KafkaMsg): Unit = {
    Thread.sleep(5000)
    val done = Source
      .single(msg)
      .map(value => new ProducerRecord[String, String](topicName, value.asJson.noSpaces))
      .runWith(Producer.plainSink(settingsWithProducer))
    done onComplete {
      case Success(value) =>
        // close the producer after use
        //      kafkaProducer.close()
        println("KafkaProducer Response: " + value)
      case Failure(ex)    =>
        println("Got error: " + ex)
    }
  }
}

//object KafkaProducerService {
//  def apply(topicName: String)(implicit system: ActorSystem[SpawnProtocol.Command]) = new KafkaProducerService(topicName)
//}
