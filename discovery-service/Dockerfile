FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY discovery-service/pom.xml discovery-service/
RUN ./mvnw dependency:go-offline -pl discovery-service
COPY discovery-service/src discovery-service/src
RUN ./mvnw package -pl discovery-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/discovery-service/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
