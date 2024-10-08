FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml ./

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
RUN apk add --no-cache maven

WORKDIR /app

COPY --from=build /app/target/sdm-bahteramas-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]