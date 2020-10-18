#!/usr/bin/env sh

rsync -a build/ raspberrypi:/home/pi/raspi-humidity-service/ \
  --exclude classes \
  --exclude generated \
  --exclude quarkus-app \
  --exclude resources \
  --exclude tmp