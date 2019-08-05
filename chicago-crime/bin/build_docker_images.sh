#!/usr/bin/env bash

echo "Building spark image"
docker build \
  -t spark:geospatial \
  -f Dockerfile.spark .

echo "Building zeppelin image"
docker build \
  -t zeppelin:geospatial \
  -f Dockerfile.zeppelin .
