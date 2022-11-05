FROM gradle:7.5.1-jdk17-jammy AS builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM amazoncorretto:17-alpine3.15

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/ /app/

ENTRYPOINT ["java", "-jar", "/app/unq-eis-gav-api-1.0-SNAPSHOT.jar"]
