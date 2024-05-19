#! /bin/bash

ROOT_PATH="/home/ubuntu/spring-github-action"
SERVICE_JAVA_PID=$(pgrep java)

if [ -n "$SERVICE_JAVA_PID" ]; then
  sudo kill -9 "$SERVICE_JAVA_PID"
fi

echo -e "\n stop 실행" >> test.txt
