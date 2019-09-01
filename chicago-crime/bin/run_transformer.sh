#!/usr/bin/env bash

set -a
source ../local.env
set +a

${SPARK_HOME}/bin/spark-submit \
  --master "${SPARK_MASTER}" \
  --deploy-mode "${SPARK_DEPLOY_MODE}" \
  --class "ChicagoCrime.Main" \
  --conf "spark.jars.packages=org.apache.hadoop:hadoop-aws:2.7.3,com.amazonaws:aws-java-sdk:1.7.4,org.datasyslab:geospark:1.2.0,org.datasyslab:geospark-viz_2.3:1.2.0,org.datasyslab:geospark-sql_2.3:1.2.0,org.datasyslab:sernetcdf:0.1.0,mrpowers:spark-daria:0.31.0-s_2.11" \
  --conf "spark.executor.memory=${SPARK_MEMORY}" \
  --conf "spark.driver.memory=${SPARK_MEMORY}" \
  --conf "spark.executor.instances=${SPARK_EXECUTOR_INSTANCES}" \
  --conf "spark.kubernetes.authenticate.driver.serviceAccountName=spark" \
  --conf "spark.kubernetes.container.image=docker.io/eschizoid/spark:geospatial" \
  --conf "spark.kubernetes.driverEnv.AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}" \
  --conf "spark.kubernetes.driverEnv.AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}" \
  --conf "spark.kubernetes.driverEnv.SPARK_MASTER=${SPARK_MASTER}" \
  /Users/admin/development/spark-playground/chicago-crime/build/libs/chicago-crime-1.0-SNAPSHOT-all.jar
