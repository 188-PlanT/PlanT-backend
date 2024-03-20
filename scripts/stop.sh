ROOT_PATH="/home/ubuntu/spring-github-action"
JAR="$ROOT_PATH/application.jar"
STOP_LOG="$ROOT_PATH/stop.log"
SERVICE_PID=$(pgrep -f $JAR) # 실행중인 Spring 서버의 PID
SERVICE_JAVA_PID=$(pgrep java)

if [ -n "$SERVICE_PID" ]; then
  kill "$SERVICE_PID"
if [ -n "$SERVICE_JAVA_PID" ]; then
  kill -9 "$SERVICE_JAVA_PID"
  # kill -9 $SERVICE_PID # 강제 종료를 하고 싶다면 이 명령어 사용
fi
