FROM openjdk:17-jdk-slim as builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle/
COPY settings.gradle .
COPY . . 

# 의존성 다운로드 및 빌드
RUN ./gradlew :module-application:bootJar -x test 


# 2. 실행 스테이지
FROM openjdk:17-jdk-slim

#COPY --from=builder /app/module-application/build/libs/*.jar app.jar
COPY --from=builder /app/module-application/build/libs/module-application-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너 실행 시 실행될 명령 정의 (Spring Boot 앱 실행)
ENTRYPOINT ["java", "-jar", "app.jar"]