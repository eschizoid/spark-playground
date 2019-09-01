package CassandraDriver

import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SparkSession}

trait SparkSupport {
  val spark: SparkSession = SparkSession.builder
    .appName("jdbc-driver")
    .master("local[*]")
    .getOrCreate()

  @transient val sc: SparkContext = spark.sparkContext
  val sqlContext: SQLContext = spark.sqlContext

  private def init(): Unit = {
    sc.setLogLevel("INFO")
  }

  init()

  def close(): Unit = {
    spark.close()
  }
}
