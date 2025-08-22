FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/Chat-0.0.1-SNAPSHOT.jar Chat.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Chat.jar"]
