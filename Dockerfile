# Use Maven to build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Use OpenJDK for the runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/useranalytics-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
