import logging

from pyspark.streaming import DStream
from pyspark.streaming.kinesis import KinesisUtils

from python.spark_support import SparkSupport


class KinesisConsumer(SparkSupport):

    def __init__(self):
        super().__init__()
        self.ssc = self.get_streaming_context()

    def get_kinesis_stream(self) -> DStream:
        logging.info("Starting up consumer...")
        kinesis_stream = KinesisUtils.createStream(
            ssc=self.ssc,
            kinesisAppName="iot-device",
            streamName="iot-device",
            endpointUrl="kinesis.us-east-1.amazonaws.com",
            checkpointInterval=60,
            initialPositionInStream=0,
            regionName="")
        return kinesis_stream
