package ChicagoCrime

import com.github.mrpowers.spark.daria.sql.EtlDefinition
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import com.github.mrpowers.spark.daria.sql.DataFrameExt._

class Transformer extends SparkSupport {
  private val schema = StructType(
    List(
      StructField("ID", StringType, nullable = true),
      StructField("Case Number", StringType, nullable = true),
      StructField("Date", StringType, nullable = true),
      StructField("Block", StringType, nullable = true),
      StructField("IUCR", StringType, nullable = true),
      StructField("Primary Type", StringType, nullable = true),
      StructField("Description", StringType, nullable = true),
      StructField("Location Description", StringType, nullable = true),
      StructField("Arrest", StringType, nullable = true),
      StructField("Domestic", StringType, nullable = true),
      StructField("Beat", StringType, nullable = true),
      StructField("District", StringType, nullable = true),
      StructField("Ward", StringType, nullable = true),
      StructField("Community Area", StringType, nullable = true),
      StructField("FBI Code", StringType, nullable = true),
      StructField("X Coordinate", StringType, nullable = true),
      StructField("Y Coordinate", StringType, nullable = true),
      StructField("Year", StringType, nullable = true),
      StructField("Updated On", StringType, nullable = true),
      StructField("Latitude", StringType, nullable = true),
      StructField("Longitude", StringType, nullable = true),
      StructField("Location", StringType, nullable = true),
      StructField("Historical Wards 2003-2015", StringType, nullable = true),
      StructField("Zip Codes", StringType, nullable = true),
      StructField("Community Areas", StringType, nullable = true),
      StructField("Census Tracts", StringType, nullable = true),
      StructField("Wards", StringType, nullable = true),
      StructField("Boundaries - ZIP Codes", StringType, nullable = true),
      StructField("Police Districts", StringType, nullable = true),
      StructField("Police Beats", StringType, nullable = true)
    )
  )

  private val csv = spark.readStream
    .format("csv")
    .schema(schema)
    .option("delimiter", ",")
    .option("header", "true")
    .load("s3a://spark-playground-datasets/chicago-crime/gold")

  private val etl = EtlDefinition(
    sourceDF = csv,
    transform = parquetTransformer(),
    write = parquetStreamWriter(
      s"s3a://spark-playground-datasets/chicago-crime/silver",
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

object Transformer {
  def apply() = new Transformer()
}
