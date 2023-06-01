FROM openjdk:17-jdk-slim

RUN apt-get update && \
    apt-get install -y maven

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN mvn clean install spring-boot:repackage

EXPOSE 8020

CMD ["java", "-jar", "target/Inventory-0.0.1-SNAPSHOT.jar"]
