import logging

from pyspark.streaming import DStream
from pyspark.streaming.context import StreamingContext
from pyspark.streaming.kinesis import KinesisUtils, InitialPositionInStream
from singleton_decorator import singleton


@singleton
class KinesisConsumer:

    def __init__(self, sc):
        self.sc = sc
        self.ssc = StreamingContext(self.sc, 1)

    def get_kinesis_stream(self) -> DStream:
        logging.info("Starting up consumer...")
        kinesis_stream = KinesisUtils.createStream(
            ssc=self.ssc,
            kinesisAppName="iot-device",
            streamName="iot-device",
            endpointUrl="kinesis.us-east-1.amazonaws.com",
            checkpointInterval=60,
            initialPositionInStream=InitialPositionInStream.LATEST,
            regionName="us-east-1")
        return kinesis_stream
