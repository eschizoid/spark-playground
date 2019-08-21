import logging
import os

from pyspark.streaming.context import StreamingContext


class KinesisConsumer:

    def __init__(self, sc, spark):
        self.sc = sc
        self.spark = spark
        self.ssc = StreamingContext(self.sc, 1)

    def get_kinesis_stream(self):
        logging.info("Starting up consumer...")
        kinesis_stream = self.spark.readStream \
            .format("kinesis") \
            .option("streamName", "iot-device") \
            .option("endpointUrl", "https://kinesis.us-east-1.amazonaws.com") \
            .option("awsAccessKeyId", os.getenv("AWS_ACCESS_KEY_ID")) \
            .option("awsSecretKey", os.getenv("AWS_SECRET_ACCESS_KEY")) \
            .option("startingposition", "EARLIEST") \
            .load()
        return kinesis_stream
