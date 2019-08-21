from __future__ import print_function

import mysql.connector
from pyspark.sql import Row
from singleton_decorator import singleton


@singleton
class MySQLSink:

    def __init__(self):
        self.cnx = mysql.connector.connect(user='root',
                                           password='root',
                                           host='mysql',
                                           database='measure')
        self.cursor = self.cnx.cursor()

    def insert_record(self, row: Row):
        if row is not None:
            record = [
                row['measure_id'],
                row['sensor_id'],
                row['current_temperature'],
                row['status']
            ]

            self.cursor.execute(
                "insert into temperature (measure_id, sensor_id, current_temperature, status) values(%s,%s,%s,%s)",
                record
            )
            self.cnx.commit()
            # self.cursor.close()
            # self.cnx.close()
