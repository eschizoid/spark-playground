from pyspark.sql import Row
from pyspark.sql.context import SQLContext
from pyspark.sql.functions import col, get_json_object
from pyspark.sql.types import StringType, StructType, StructField, IntegerType

from python.mysql_sink import MySQLSink


class Transformer:

    def __init__(self, sc, spark):
        self.sc = sc
        self.spark = spark
        self.sql_context = SQLContext(self.sc)
        self.schema = StructType([
            StructField("sensor_id", StringType(), False),
            StructField("currentTemperature", IntegerType(), False),
            StructField("status", StringType(), False)
        ])

    @staticmethod
    def transform_stream(kinesis_stream):
        kinesis_stream \
            .selectExpr("CAST(sequenceNumber AS STRING)", "CAST(data AS STRING)") \
            .withColumn("value", col("data").cast(StringType())) \
            .withColumn("measure_id", col("sequenceNumber").cast(StringType())) \
            .select(
                col("measure_id"),
                get_json_object("value", "$.sensorId").alias("sensor_id"),
                get_json_object("value", "$.currentTemperature").alias("current_temperature"),
                get_json_object("value", "$.status").alias("status")
            ) \
            .writeStream \
            .foreach(lambda rdd: rdd.foreach(process_row)) \
            .start()


def process_row(row: Row):
    sink = MySQLSink()
    sink.insert_record(row)
