package FlightDelayAnalysis

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape, _}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._

/**
  * ==Sample Output==
  * {{{
  * Delays for carrier OO: 27 average minutes, 938073 delayed flights
  * Delays for carrier AA: 34 average minutes, 1183799 delayed flights
  * Delays for carrier MQ: 33 average minutes, 930056 delayed flights
  * Delays for carrier DH: 30 average minutes, 54817 delayed flights
  * Delays for carrier FL: 33 average minutes, 434598 delayed flights
  * Delays for carrier TZ: 33 average minutes, 27302 delayed flights
  * Delays for carrier DL: 27 average minutes, 948978 delayed flights
  * Delays for carrier HP: 22 average minutes, 82647 delayed flights
  * Delays for carrier NW: 27 average minutes, 838956 delayed flights
  * Delays for carrier UA: 35 average minutes, 862799 delayed flights
  * Delays for carrier 9E: 32 average minutes, 197376 delayed flights
  * Delays for carrier CO: 32 average minutes, 582315 delayed flights
  * Delays for carrier XE: 35 average minutes, 720356 delayed flights
  * Delays for carrier AQ: 14 average minutes, 25368 delayed flights
  * Delays for carrier EV: 36 average minutes, 551691 delayed flights
  * Delays for carrier AS: 29 average minutes, 297897 delayed flights
  * Delays for carrier F9: 21 average minutes, 161064 delayed flights
  * Delays for carrier B6: 39 average minutes, 301876 delayed flights
  * Delays for carrier WN: 25 average minutes, 1811278 delayed flights
  * Delays for carrier OH: 30 average minutes, 480813 delayed flights
  * Delays for carrier HA: 16 average minutes, 55886 delayed flights
  * Delays for carrier YV: 38 average minutes, 361585 delayed flights
  * Delays for carrier US: 27 average minutes, 844017 delayed flights
  * }}}
  */
object Main extends Logging {

  implicit val system: ActorSystem             = ActorSystem("flight-delay-analysis")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val flightProcessor                          = FlightDelayProcessor()

  def main(args: Array[String]): Unit = {
    logger.debug("Starting Akka Streams application...")

    // Sink
    val sink = Sink.foreach(flightProcessor.averageSink)

    val g = RunnableGraph.fromGraph(GraphDSL.create(sink) { implicit builder => S =>
      import GraphDSL.Implicits._

      // Source
      val A: Outlet[String] = builder.add(Source.fromIterator(() => flightProcessor.readCsvFiles())).out

      // Flows
      val B: FlowShape[String, FlightEvent]                           = builder.add(flightProcessor.csvToFlightEvent())
      val C: FlowShape[FlightEvent, FlightDelayRecord]                = builder.add(flightProcessor.filterAndConvert())
      val D: UniformFanOutShape[FlightDelayRecord, FlightDelayRecord] = builder.add(Broadcast[FlightDelayRecord](1))
      val F: FlowShape[FlightDelayRecord, (String, Int, Int)]         = builder.add(flightProcessor.averageCarrierDelay())

      // Graph
      A ~> B ~> C ~> D ~> F ~> S
      ClosedShape
    })

    val future = g.run()
    future.onComplete { _ =>
      system.terminate()
    }
    Await.result(system.whenTerminated, Duration.Inf)
  }
}
