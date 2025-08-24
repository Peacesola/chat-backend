FROM maven:3.8.3-openjdk-17 as build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk
COPY --from=build /target/Chat-0.0.1-SNAPSHOT.jar Chat.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Chat.jar"]