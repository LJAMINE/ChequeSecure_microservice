FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY gateway-service/pom.xml gateway-service/
RUN ./mvnw dependency:go-offline -pl gateway-service
COPY gateway-service/src gateway-service/src
RUN ./mvnw package -pl gateway-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/gateway-service/target/*.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]
