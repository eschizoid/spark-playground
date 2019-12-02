package FlightDelayAnalysis

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape, _}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._

object Main extends Logging {

  implicit val system: ActorSystem             = ActorSystem("flight-delay-analysis")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val flightProcessor: FlightDelayProcessor    = FlightDelayProcessor()

  def main(args: Array[String]): Unit = {
    logger.debug("Starting Akka Streams application...")

    // Sink
    val sink = Sink.foreach(flightProcessor.averageSink[(String, Int, Int)])

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
