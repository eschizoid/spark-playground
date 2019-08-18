from pyspark.sql.functions import get_json_object
from singleton_decorator import singleton


@singleton
class MySQLProducer:

    @staticmethod
    def sink_stream(dstream) -> None:
        dstream.select(
            get_json_object(dstream["value"], "$.sensorId").alias("sensor_id"),
            get_json_object(dstream["value"], "$.currentTemperature").alias("current_temperature"),
            get_json_object(dstream["value"], "$.status").alias("status")
        ).write.jdbc(
            url="jdbc:mysql://mysql:3306/measure",
            table='temperature',
            mode='append',
            properties={'user': 'root', 'password': 'root', 'driver': 'com.mysql.jdbc.Driver'}
        )
