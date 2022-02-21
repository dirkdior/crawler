package db.kafka

object KafkaServiceProtocol {

  sealed trait Command
  object RandomCommand extends Command

}
