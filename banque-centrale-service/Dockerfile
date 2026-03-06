FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY banque-centrale-service/pom.xml banque-centrale-service/
RUN ./mvnw dependency:go-offline -pl banque-centrale-service
COPY banque-centrale-service/src banque-centrale-service/src
RUN ./mvnw package -pl banque-centrale-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/banque-centrale-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
