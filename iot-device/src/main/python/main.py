from python.spark_runner import SparkRunner


def main():
    runner = SparkRunner()
    temperature = runner.kinesis_consumer.get_kinesis_stream()
    temperature.foreachRDD(runner.process_rdd)
    runner.kinesis_consumer.ssc.start()
    runner.kinesis_consumer.ssc.awaitTermination()


if __name__ == "__main__":
    main()
