import logging
import os

from pyspark import SparkContext
from pyspark.sql.session import SparkSession


class SparkSupport:

    def __init__(self):
        spark = SparkSession.builder \
            .master(os.getenv("SPARK_MASTER")) \
            .getOrCreate()
        self.spark = spark
        self.sc = spark.sparkContext
        logging.info("Spark driver version: " + self.sc.version)

    def get_spark_context(self) -> SparkContext:
        return self.sc

    def get_spark_session(self) -> SparkSession:
        return self.spark
