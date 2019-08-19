#!/usr/bin/env bash

echo "Building spark image"
docker build \
  -t spark:iot-device \
  -f Dockerfile .
