#!/usr/bin/env bash

set -a
source ../local.env
set +a

/opt/spark/bin/spark-submit \
  --master "${SPARK_MASTER}" \
  --deploy-mode "${SPARK_DEPLOY_MODE}" \
  --class "ChicagoCrime.Main" \
  --conf "spark.executor.memory=${SPARK_MEMORY}" \
  --conf "spark.driver.memory=${SPARK_MEMORY}" \
  --conf "spark.executor.instances=${SPARK_EXECUTOR_INSTANCES}" \
  --conf "spark.kubernetes.authenticate.driver.serviceAccountName=spark" \
  --conf "spark.kubernetes.container.image=docker.io/eschizoid/spark:geospatial" \
  --conf "spark.kubernetes.driverEnv.AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}" \
  --conf "spark.kubernetes.driverEnv.AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}" \
  /Users/admin/development/spark-playground/chicago-crime/build/libs/chicago-crime-1.0-SNAPSHOT-all.jar
  #local:///opt/spark/examples/chicago-crime-1.0-SNAPSHOT-all.jar

