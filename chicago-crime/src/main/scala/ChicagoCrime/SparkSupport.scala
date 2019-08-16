package ChicagoCrime

import org.apache.spark.SparkContext
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator
import org.datasyslab.geosparksql.utils.GeoSparkSQLRegistrator
import org.datasyslab.geosparkviz.sql.utils.GeoSparkVizRegistrator

trait SparkSupport {
  val spark: SparkSession = SparkSession.builder
    .appName("chicago-crime")
    .master(sys.env("SPARK_MASTER"))
    .config("spark.serializer", classOf[KryoSerializer].getName)
    .config("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)
    .getOrCreate()

  @transient val sc: SparkContext = spark.sparkContext
  val sqlContext: SQLContext      = spark.sqlContext

  private def init(): Unit = {
    GeoSparkSQLRegistrator.registerAll(spark)
    GeoSparkVizRegistrator.registerAll(spark)

    sc.setLogLevel("INFO")
    sc.hadoopConfiguration.set("fs.s3a.access.key", sys.env("AWS_ACCESS_KEY_ID"))
    sc.hadoopConfiguration.set("fs.s3a.secret.key", sys.env("AWS_SECRET_ACCESS_KEY"))
    sc.hadoopConfiguration.set("fs.s3a.fast.upload", "true")
  }

  init()

  def close(): Unit = {
    spark.close()
  }
}
