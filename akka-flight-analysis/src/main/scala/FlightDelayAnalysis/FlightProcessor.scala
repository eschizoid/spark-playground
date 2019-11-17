package FlightDelayAnalysis

import java.io.File

import akka.NotUsed
import akka.stream.scaladsl.Flow

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.io.Source
import scala.util.Try

trait FlightProcessor {
  def readCsvFiles(): Iterator[String]
  def csvToFlightEvent(): Flow[String, FlightEvent, NotUsed]
  def filterAndConvert(): Flow[FlightEvent, FlightDelayRecord, NotUsed]
  def averageCarrierDelay(): Flow[FlightDelayRecord, (String, Int, Int), NotUsed]
  def averageSink[T](tuple: T): Unit
}

class FlightDelayProcessor extends FlightProcessor with Logging {

  def readCsvFiles(): Iterator[String] = {
    new File(
      "/Users/admin/development/spark-playground/akka-flight-analysis/src/test/resources/csv"
    ).listFiles
      .filter(_.getName.endsWith(".csv"))
      .map(file => Source.fromFile(file.getAbsoluteFile, "utf-8").getLines())
      .foldLeft(Iterator[String]())(_ ++ _)
  }

  def csvToFlightEvent(): Flow[String, FlightEvent, NotUsed] = {
    Flow[String]
      .map(_.split(",").map(_.trim)) // we now have our columns split by ","
      .map(
        cols =>
          FlightEvent(
            cols(0),
            cols(1),
            cols(2),
            cols(3),
            cols(4),
            cols(5),
            cols(6),
            cols(7),
            cols(8),
            cols(9),
            cols(10),
            cols(11),
            cols(12),
            cols(13),
            cols(14),
            cols(15),
            cols(16),
            cols(17),
            cols(18),
            cols(19),
            cols(20),
            cols(21),
            cols(22),
            cols(23),
            cols(24),
            cols(25),
            cols(26),
            cols(27),
            cols(28)
        )
      )
  }

  def filterAndConvert(): Flow[FlightEvent, FlightDelayRecord, NotUsed] = {
    Flow[FlightEvent]
      .filter(r => Try(r.arrDelayMins.toInt).getOrElse(-1) > 0) // convert arrival delays to ints, filter out non delays
      .mapAsyncUnordered(parallelism = 15) { flight =>
        Future(
          FlightDelayRecord(
            flight.year,
            flight.month,
            flight.dayOfMonth,
            flight.flightNum,
            flight.uniqueCarrier,
            flight.arrDelayMins
          )
        )
      }
  }

  def averageCarrierDelay(): Flow[FlightDelayRecord, (String, Int, Int), NotUsed] = {
    Flow[FlightDelayRecord]
      .groupBy(30, _.uniqueCarrier)
      .fold(("", 0, 0)) { (x: (String, Int, Int), y: FlightDelayRecord) =>
        val count        = x._2 + 1
        val totalMinutes = x._3 + Try(y.arrDelayMins.toInt).getOrElse(0)
        (y.uniqueCarrier, count, totalMinutes)
      }
      .mergeSubstreams
  }

  def averageSink[T](tuple: T): Unit = {
    tuple match {
      case (a: String, b: Int, c: Int) =>
        logger.debug(
          s"Delays for carrier $a: ${Try(c / b).getOrElse(0)} average minutes, $b delayed flights"
        )
      case x => logger.error("no idea what " + x + "is!")
    }
  }
}

object FlightDelayProcessor {
  def apply() = new FlightDelayProcessor()
}
