# 빌드를 위한 Gradle 이미지 사용
FROM gradle:8.5-jdk17 AS builder
WORKDIR /build
COPY . .
RUN chmod +x gradlew   # gradlew 실행 권한 부여
RUN ./gradlew build --no-daemon -x test

# 실행용 Java 이미지
FROM eclipse-temurin:17-jre
WORKDIR /app

# Timezone 설정 (서울로 설정)
ENV TZ=Asia/Seoul

# .env 파일에서 전달된 환경 변수를 사용하여 Java 애플리케이션 실행
# 예시로 SPRING_PROFILES_ACTIVE 환경 변수를 사용하는 경우
ARG SPRING_PROFILES_ACTIVE=prod
# default로 prod 설정
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# 빌드 결과물인 JAR 파일을 컨테이너에 복사
COPY --from=builder /build/build/libs/*.jar app.jar

# EXPOSE로 컨테이너가 사용할 포트를 열어줍니다.
EXPOSE 8080
EXPOSE 8084
# ENTRYPOINT에서 환경 변수를 사용하여 Java 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]