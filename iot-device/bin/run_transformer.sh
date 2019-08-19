#!/usr/bin/env bash

set -a
source ../local.env
set +a

${SPARK_HOME}/bin/spark-submit \
  --master "${SPARK_MASTER}" \
  --deploy-mode "${SPARK_DEPLOY_MODE}" \
  --conf "spark.executor.memory=${SPARK_MEMORY}" \
  --conf "spark.driver.memory=${SPARK_MEMORY}" \
  --conf "spark.executor.instances=${SPARK_EXECUTOR_INSTANCES}" \
  --conf "spark.kubernetes.authenticate.driver.serviceAccountName=spark" \
  --conf "spark.kubernetes.container.image=docker.io/eschizoid/spark:iot-device" \
  --conf "spark.kubernetes.driverEnv.AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}" \
  --conf "spark.kubernetes.driverEnv.AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}" \
  --conf "spark.kubernetes.driverEnv.SPARK_MASTER=${SPARK_MASTER}" \
  --conf "spark.kubernetes.pyspark.pythonVersion=3" \
  local:///opt/spark/examples/iot-device/main.py
