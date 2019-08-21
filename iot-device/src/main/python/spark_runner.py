from python.kinesis_consumer import KinesisConsumer
from python.spark_support import SparkSupport
from python.transformer import Transformer


class SparkRunner:
    def __init__(self):
        spark_support = SparkSupport()
        self.sc = spark_support.get_spark_context()
        self.spark = spark_support.get_spark_session()
        self.kinesis_consumer = KinesisConsumer(self.sc, self.spark)
        self.transformer = Transformer(self.sc, self.spark)

    def run(self) -> None:
        temperature_stream = self.kinesis_consumer.get_kinesis_stream()
        self.transformer.transform_stream(temperature_stream)
        self.kinesis_consumer.ssc.awaitTermination()
