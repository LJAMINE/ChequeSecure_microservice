FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY config-service/pom.xml config-service/
RUN ./mvnw dependency:go-offline -pl config-service
COPY config-service/src config-service/src
RUN ./mvnw package -pl config-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/config-service/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
