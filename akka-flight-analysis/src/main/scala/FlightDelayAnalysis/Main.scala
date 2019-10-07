package FlightDelayAnalysis

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Broadcast, GraphDSL, RunnableGraph, Sink, Source}

object Main {

  implicit val system       = ActorSystem("flight-delay-analysis")
  implicit val materializer = ActorMaterializer()
  val flightProcessor       = FlightDelayProcessor()

  def main(args: Array[String]): Unit = {
    // @formatter:off
    val g = RunnableGraph.fromGraph(GraphDSL.create() {

      implicit builder =>
        import GraphDSL.Implicits._

        // Source
        val A: Outlet[String] = builder.add(Source.fromIterator(() =>   flightProcessor.readCsvFiles())).out

        // Flows
        val B: FlowShape[String, FlightEvent] = builder.add(flightProcessor.csvToFlightEvent())
        val C: FlowShape[FlightEvent, FlightDelayRecord] = builder.add(flightProcessor.filterAndConvert())
        val D: UniformFanOutShape[FlightDelayRecord, FlightDelayRecord] = builder.add(Broadcast[FlightDelayRecord](2))
        val F: FlowShape[FlightDelayRecord, (String, Int, Int)] = builder.add(flightProcessor.averageCarrierDelay())

        // Sinks
        val E: Inlet[Any] = builder.add(Sink.ignore).in
        val G: Inlet[Any] = builder.add(Sink.foreach(flightProcessor.averageSink)).in

        // Graph
        A ~> B ~> C ~> D
        E <~ D
        G <~ F <~ D

        ClosedShape
    })
    // @formatter:on

    g.run()
  }
}
