package ChicagoCrime

import com.github.mrpowers.spark.daria.sql.EtlDefinition
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import com.github.mrpowers.spark.daria.sql.DataFrameExt._

class ChicagoCrimeStreamWriter extends SparkSupport {

  private val csv = spark.readStream
    .format("csv")
    .schema(Schemas.crime)
    .option("delimiter", ",")
    .option("header", "true")
    .load("s3a://spark-playground-datasets/chicago-crime/gold/Crimes_-_2001_to_present.csv")

  private val etl = EtlDefinition(
    sourceDF = csv,
    transform = parquetTransformer(),
    write = parquetStreamWriter(
      s"s3a://spark-playground-datasets/chicago-crime/silver/crime_history",
      "chicago-crime-checkpoint"
    )
  )

  import sqlContext.implicits._

  private def parquetTransformer()(df: DataFrame): DataFrame = {
    df.renameColumns(_.toLowerCase)
      .renameColumns(_.replace(" - ", "_"))
      .renameColumns(_.replace("-", "_"))
      .renameColumns(_.replaceAll(" ", "_"))
      .repartition($"year")
  }

  private def parquetStreamWriter(dataPath: String, checkpointPath: String)(df: DataFrame): Unit = {
    val query = df.writeStream
      .option("checkpointLocation", checkpointPath)
      .option("path", dataPath)
      .outputMode(OutputMode.Append)
      .partitionBy("year")
      .start()

    query.awaitTermination()
  }

  def start() {
    etl.process()
  }
}

object ChicagoCrimeStreamWriter {
  def apply() = new CommunityAreaWriter()
}
