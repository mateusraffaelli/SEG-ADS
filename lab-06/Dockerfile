FROM gradle:9-jdk25 AS builder
WORKDIR /app
COPY ./lab-05 .
RUN gradle bootJar --no-daemon -x test
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]