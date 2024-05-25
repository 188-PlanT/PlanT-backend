# open jdk 17 버전의 환경을 구성한다.
FROM openjdk:11-jdk

#빌드 될때 jar 파일 위치 선언
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

# 운영 및 개발에서 사용되는 환경 설정을 분리한다.
ENTRYPOINT ["java", "-jar", "-D spring.profiles.active=prod", "/app.jar"]