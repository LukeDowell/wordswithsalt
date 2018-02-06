#!/usr/bin/env bash

DOCKER_IMAGE_NAME="wordswithsalt"
DOCKER_IMAGE_PATH="wordswithsalt-backend/Dockerfile"
NODE_COUNT=3

# Clean and build a fresh version of our backend Jar
./gradlew wordswithsalt-backend:clean wordswithsalt-backend:build

# Build the docker image
docker build --compress --tag ${DOCKER_IMAGE_NAME} --file ${DOCKER_IMAGE_PATH} ./wordswithsalt-backend

# Start the cluster and attach to it's output
exec docker-compose up --scale cluster-juan=${NODE_COUNT}