#!/bin/bash

ROOT_PATH="/home/ubuntu/spring-github-action"
JAR="$ROOT_PATH/application.jar"

APP_LOG="$ROOT_PATH/application.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

NOW=$(date +%c)

#echo "[$NOW] $JAR 복사" >> $START_LOG
#cp $ROOT_PATH/build/libs/blazingDevs_calendar-0.0.1-SNAPSHOT.jar $JAR

#echo "[$NOW] > $JAR 실행" >> $START_LOG
#nohup java -jar $JAR --spring.profiles.active=prod & #> $APP_LOG 2> $ERROR_LOG
#./gradlew build
nohup java -jar $ROOT_PATH/build/libs/blazingDevs_calendar-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > /dev/null 2> /dev/null < /dev/null &

SERVICE_PID=$(pgrep -f $JAR)
#echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG
