from pyspark.sql.context import SQLContext
from pyspark.sql.functions import get_json_object, monotonically_increasing_id, col
from pyspark.sql.types import StringType
from singleton_decorator import singleton


@singleton
class MySQLProducer:

    def __init__(self, sc):
        self.sc = sc
        self.sql_context = SQLContext(self.sc)

    def sink_stream(self, rdd) -> None:
        temperature_df = self.sql_context.createDataFrame(rdd, StringType()) \
            .withColumn("measure_id", monotonically_increasing_id())
        temperature_df.select(
            col("measure_id"),
            get_json_object(temperature_df["value"], "$.sensorId").alias("sensor_id"),
            get_json_object(temperature_df["value"], "$.currentTemperature").alias("current_temperature"),
            get_json_object(temperature_df["value"], "$.status").alias("status")
        ).write.jdbc(
            url="jdbc:mysql://mysql:3306/measure",
            table='temperature',
            mode='append',
            properties={'user': 'root', 'password': 'root', 'driver': 'com.mysql.jdbc.Driver'}
        )
