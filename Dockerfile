FROM maven:3.9.9-eclipse-temurin-21 AS build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /target/Chat-0.0.1-SNAPSHOT.jar Chat.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Chat.jar"]