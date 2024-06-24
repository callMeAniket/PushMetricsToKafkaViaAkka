import akka.actor.{Actor, ActorRef, Props}

class MetricGenerator(kafkaProducer: ActorRef) extends Actor {
  import context.dispatcher

  import scala.concurrent.duration._

  override def preStart(): Unit = {
    context.system.scheduler.scheduleWithFixedDelay(0.seconds, 5.seconds, self, "generate")
  }

  def receive: Receive = {
    case "generate" =>
      val metric = Metric.randomMetric()
      kafkaProducer ! Metric.toJson(metric)
  }
}

object MetricGenerator extends App{
  def props(kafkaProducer: ActorRef): Props = Props(new MetricGenerator(kafkaProducer))
}
