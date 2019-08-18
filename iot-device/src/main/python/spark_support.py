import logging
import os
from functools import wraps

from pyspark import SparkContext, SparkConf
from pyspark.sql.context import SQLContext
from pyspark.streaming import StreamingContext

__instances = {}


def singleton(cls):
    @wraps(cls)
    def get_instance(*args, **kwargs):
        instance = __instances.get(cls, None)
        if not instance:
            instance = cls(*args, **kwargs)
            __instances[cls] = instance
        return instance

    return get_instance


@singleton
class SparkSupport:

    def __init__(self):
        conf = SparkConf()
        conf.setAppName("iot-device")
        conf.setMaster(os.getenv("SPARK_MASTER"))
        sc = SparkContext(conf=conf)
        ssc = StreamingContext(sc, 60)
        sql = SQLContext(sc)
        logging.info("Spark driver version: " + sc.version)
        hadoop_conf = sc._jsc.hadoopConfiguration()
        hadoop_conf.set("fs.s3.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
        hadoop_conf.set("fs.s3.awsAccessKeyId", os.getenv("AWS_ACCESS_KEY_ID"))
        hadoop_conf.set("fs.s3.awsSecretAccessKey", os.getenv("AWS_SECRET_ACCESS_KEY"))
        hadoop_conf.set("fs.s3a.fast.upload", "true")
        sc.setLogLevel(os.getenv("LOGGING_LEVEL"))
        ssc.checkpoint("checkpoint-iot-devices")
        self.ssc = ssc
        self.sql = sql
        self.sc = sc

    def get_spark_context(self) -> SparkContext:
        return self.sc

    def get_sql_context(self) -> SQLContext:
        return self.sql

    def get_streaming_context(self) -> StreamingContext:
        return self.ssc
