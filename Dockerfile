FROM amazoncorretto:21 AS builder

WORKDIR /app

COPY app/gradle gradle
COPY app/gradlew gradlew
COPY app/build.gradle.kts app/settings.gradle.kts ./

RUN chmod +x ./gradlew

COPY app/src src

RUN ./gradlew clean build test --no-daemon

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]