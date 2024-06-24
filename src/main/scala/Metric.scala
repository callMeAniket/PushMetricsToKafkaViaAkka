import spray.json.DefaultJsonProtocol.{DoubleJsonFormat, StringJsonFormat, jsonFormat5}
import spray.json.{RootJsonFormat, enrichAny}

import java.time.Instant

case class Metric(metricName: String, value: Double, timestamp: String, host: String, region: String)

object Metric {

  def randomMetric(): Metric = {
    val metricNames = Seq("cpu_usage_percentage", "memory_usage_gb", "disk_io_rate_mbps", "network_throughput_mbps", "response_time_ms", "error_rate_percentage")
    val hosts = Seq("server01", "server02", "server03", "server04", "server05")
    val regions = Seq("us-east-1", "us-west-2", "eu-west-1", "eu-central-1", "ap-south-1")

    val metricName = metricNames(scala.util.Random.nextInt(metricNames.size))
    val value = metricName match {
      case "cpu_usage_percentage" => scala.util.Random.nextDouble() * 100
      case "memory_usage_gb" => scala.util.Random.nextDouble() * 64
      case "disk_io_rate_mbps" => scala.util.Random.nextDouble() * 500
      case "network_throughput_mbps" => scala.util.Random.nextDouble() * 1000
      case "response_time_ms" => scala.util.Random.nextDouble() * 2000
      case "error_rate_percentage" => scala.util.Random.nextDouble() * 100
    }
    val timestamp = Instant.now.toString
    val host = hosts(scala.util.Random.nextInt(hosts.size))
    val region = regions(scala.util.Random.nextInt(regions.size))

    Metric(metricName, value, timestamp, host, region)
  }

  implicit val metricFormat: RootJsonFormat[Metric] = jsonFormat5(Metric.apply)

  def toJson(metric: Metric): String = {
    metric.toJson.toString()
  }
}
