#!/usr/bin/env bash

/opt/spark/sbin/start-master.sh --webui-port 8090
/opt/spark/sbin/start-slave.sh spark://Admins-MacBook-Pro.local:7077

/opt/spark/sbin/stop-master.sh
/opt/spark/sbin/stop-slave.sh
