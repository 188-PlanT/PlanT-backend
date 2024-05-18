#! /bin/sh

ROOT_PATH="/home/ubuntu/spring-github-action"
STOP_LOG="$ROOT_PATH/stop.log"
SERVICE_JAVA_PID=$(pgrep java)

if [ -n "$SERVICE_JAVA_PID" ]; then
  sudo kill -9 "$SERVICE_JAVA_PID"
  # kill -9 $SERVICE_PID # 강제 종료를 하고 싶다면 이 명령어 사용
fi

echo -e "\n stop 실행" >> test.txt
