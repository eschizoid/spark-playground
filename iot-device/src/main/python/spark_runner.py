import os
from time import time as time_

from pyspark.rdd import RDD
from pyspark.sql.context import SQLContext
from pyspark.sql.types import StringType

from python.kinesis_consumer import KinesisConsumer
from python.mysql_sink import MySQLProducer
from python.spark_support import SparkSupport

os.environ[
    "PYSPARK_SUBMIT_ARGS"] = "--packages org.apache.spark:spark-streaming-kinesis-asl_2.11:2.4.3,mysql:mysql-connector-java:5.1.48, pyspark-shell"


class SparkRunner:
    def __init__(self):
        spark_support = SparkSupport()
        sc = spark_support.get_spark_context()
        self.kinesis_consumer = KinesisConsumer(sc)
        self.mysql_producer = MySQLProducer()

    def process_rdd(self, time: time_, rdd: RDD) -> None:
        if not rdd.isEmpty():
            sql_context = SQLContext(rdd.context)
            temperature_df = sql_context.createDataFrame(rdd, StringType())
            self.mysql_producer.sink_stream(temperature_df)
