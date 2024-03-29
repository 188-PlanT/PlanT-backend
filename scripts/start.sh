#!/bin/bash

ROOT_PATH="/home/ubuntu/spring-github-action"
JAR="$ROOT_PATH/application.jar"

APP_LOG="$ROOT_PATH/application.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

JAR=blazingDevs_calendar-0.0.1-SNAPSHOT.jar
NOW=$(date +%c)

export spring_profiles_active=prod
nohup java -jar $ROOT_PATH/build/libs/$JAR > /dev/null 2> /dev/null < /dev/null &

SERVICE_PID=$(pgrep -f $JAR)
#echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG
