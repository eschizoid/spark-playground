import json
import os

from python.kinesis_consumer import KinesisConsumer
from python.mysql_sink import MySQLSink


def main():
    os.environ[
        "PYSPARK_SUBMIT_ARGS"] = "--packages \
        org.apache.hadoop:hadoop-aws:2.7.3,\
        org.apache.spark:spark-streaming-kinesis-asl_2.11:2.4.3',\
        mysql:mysql-connector-java:5.1.48, \
        pyspark-shell"

    kinesis_consumer = KinesisConsumer()
    mysql_sink = MySQLSink()
    kinesis_stream = kinesis_consumer.get_kinesis_stream()
    record = kinesis_stream.map(lambda v: json.loads(v[1]))
    mysql_sink.sink_stream(record)
    kinesis_consumer.scc.start()
    kinesis_consumer.scc.awaitTermination()
