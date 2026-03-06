FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY commercant-service/pom.xml commercant-service/
RUN ./mvnw dependency:go-offline -pl commercant-service
COPY commercant-service/src commercant-service/src
RUN ./mvnw package -pl commercant-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/commercant-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
