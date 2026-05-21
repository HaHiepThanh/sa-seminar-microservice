#Stage 1
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

#Stage 2
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/inventory-0.1.jar app.jar

EXPOSE 8082
EXPOSE 50051

ENTRYPOINT ["java", "-jar", "app.jar"]
