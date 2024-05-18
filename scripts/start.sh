#!/bin/bash
ROOT_PATH="/home/ubuntu/spring-github-action"
JAR=blazingDevs_calendar-0.0.1-SNAPSHOT.jar

export spring_profiles_active=prod
nohup java -jar $ROOT_PATH/build/libs/$JAR > /dev/null 2> /dev/null < /dev/null &

echo -e "\n stop 실행" >> test.txt
