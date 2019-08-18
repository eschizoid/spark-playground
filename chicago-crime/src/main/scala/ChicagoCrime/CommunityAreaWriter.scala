package ChicagoCrime

import org.apache.spark.sql.types.{StringType, StructField, StructType}

class CommunityAreaWriter extends SparkSupport {
  private val schema = StructType(
    List(
      StructField("CHGOCA", StringType, nullable = true),
      StructField("ZCTA5", StringType, nullable = true),
      StructField("TOT2010", StringType, nullable = true)
    )
  )

  private val csv = spark.read
    .format("csv")
    .schema(schema)
    .option("delimiter", ",")
    .option("header", "true")
    .load("s3a://spark-playground-datasets/chicago-crime/gold/Community_area_and_zip_code_equivalency.csv")

  def start() {
    val df = sqlContext.createDataFrame(csv.rdd, schema)
    df.write
      .parquet("s3a://spark-playground-datasets/chicago-crime/silver/community_area_zip_code_equivalency")
  }
}

object CommunityAreaWriter {
  def apply() = new CommunityAreaWriter()
}
