from python.spark_support import SparkSupport


class MySQLSink(SparkSupport):

    def __init__(self):
        super().__init__()
        self.sql = self.get_sql_context()

    @staticmethod
    def sink_stream(dstream) -> None:
        sdf_props = {'user': 'root', 'password': 'pwd', 'driver': 'com.mysql.jdbc.Driver'}
        dstream.write.jdbc(
            url="jdbc:mysql://mysql:3306/measures",
            table='measures',
            mode='append',
            properties=sdf_props
        )
