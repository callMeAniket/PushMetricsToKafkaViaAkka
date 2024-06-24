import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object AkkaMicroserviceApp extends App {
  implicit val system: ActorSystem = ActorSystem("AkkaMicroservice")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val kafkaProducer = system.actorOf(KafkaProducerActor.props, "kafkaProducer")
  val metricGenerator = system.actorOf(Props(classOf[MetricGenerator], kafkaProducer), "metricGenerator")

  val route =
    path("health") {
      get {
        complete("OK")
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println("Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
