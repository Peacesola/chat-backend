FROM openjdk:24-jdk-slim AS build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/Chat-0.0.1-SNAPSHOT.jar Chat.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Chat.jar"]
