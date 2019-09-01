package CassandraDriver


object Main extends App with SparkSupport {

  val df = spark.read.format("org.apache.spark.sql.cassandra")
    .option("keyspace", "optimus")
    .option("table", "main")
    .load()

  df.collect.foreach(println)
}