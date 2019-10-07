package ChicagoCrime

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.GeneralizedLinearRegression
import org.apache.spark.sql.functions.date_format
import org.apache.spark.sql.types.IntegerType

class Training extends App with SparkSupport {

  var crimeData = spark.read
    .schema(Schemas.crime)
    .parquet("s3a://spark-playground-datasets/chicago-crime/silver")

  import spark.sqlContext.implicits._

  val crimeDataSequence = crimeData
    .withColumn("sequence", $"year" + date_format($"date", "D").cast(IntegerType))
    .groupBy("sequence", "year")
    .count()

  val crimeTrainingData = crimeDataSequence.filter("year != 2018 and year != 2017")
  val crimeTestData     = crimeDataSequence.filter("year = 2018 and year = 2017")

  val assembler = new VectorAssembler()
    .setInputCols(Array("count"))
    .setOutputCol("features")

  val trainingDataVector = assembler
    .transform(crimeTrainingData)
    .select($"features", $"sequence".as("label"))

  val glr = new GeneralizedLinearRegression()
    .setFamily("poisson")
    .setMaxIter(10)

  val model = glr.fit(trainingDataVector)

  println(s"Coefficients: ${model.coefficients}")
  println(s"Intercept: ${model.intercept}")

  val summary = model.summary
  println(s"Coefficients: ${model.coefficients}")
  println(s"Intercept: ${model.intercept}")

  summary.residuals.show()
}
