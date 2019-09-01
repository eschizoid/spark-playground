package ChicagoCrime

class CommunityAreaWriter extends SparkSupport {

  private val csv = spark.read
    .format("csv")
    .schema(Schemas.communityArea)
    .option("delimiter", ",")
    .option("header", "true")
    .load("s3a://spark-playground-datasets/chicago-crime/gold/Community_area_and_zip_code_equivalency.csv")

  def start() {
    val df = sqlContext.createDataFrame(csv.rdd, Schemas.communityArea)
    df.write
      .parquet("s3a://spark-playground-datasets/chicago-crime/silver/community_area_zip_code_equivalency")
  }
}

object CommunityAreaWriter {
  def apply() = new CommunityAreaWriter()
}
