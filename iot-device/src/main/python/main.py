import os

from python.spark_runner import SparkRunner

os.environ[
    "PYSPARK_SUBMIT_ARGS"] = "--packages mysql:mysql-connector-java:5.1.48, pyspark-shell"


def main():
    runner = SparkRunner()
    runner.run()


if __name__ == "__main__":
    main()
