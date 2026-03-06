FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY agence-bancaire-service/pom.xml agence-bancaire-service/
RUN ./mvnw dependency:go-offline -pl agence-bancaire-service
COPY agence-bancaire-service/src agence-bancaire-service/src
RUN ./mvnw package -pl agence-bancaire-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/agence-bancaire-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
