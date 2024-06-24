import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties

class KafkaProducerActor extends Actor {
  implicit val system: ActorSystem = context.system  // Ensure the ActorSystem is in scope

  private val config = ConfigFactory.load()
  private val kafkaConfig = config.getConfig("akka.kafka.producer")

  private val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getString("bootstrap.servers"))
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  // Initialize Kafka producer
  private val kafkaProducer = new KafkaProducer[String, String](props)

  private val kafkaTopic = "metrics-topic1"

  def receive: Receive = {
    case jsonString: String =>
      kafkaProducer.send(new ProducerRecord[String, String](kafkaTopic, jsonString))
  }

  override def postStop(): Unit = {
    kafkaProducer.close()
    super.postStop()
  }
}

object KafkaProducerActor extends App{
  def props: Props = Props(new KafkaProducerActor)
}
