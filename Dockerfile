# First stage - build jar
FROM maven:3.8.5-eclipse-temurin-11-alpine as base
WORKDIR /app/src
COPY pom.xml ./

# Fetch all dependencies so next time we can use
# cache instead of fetching them again
RUN mvn de.qaware.maven:go-offline-maven-plugin:resolve-dependencies

# Copy sourcecode
COPY ./ ./

# Build jar
RUN mvn clean package && \
    mv /app/src/target/booking-0.0.1-SNAPSHOT.jar /app/app.jar


# Second stage - final image
FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine as final
WORKDIR /app

# Copy built jar
COPY --from=base /app/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT java -jar /app/app.jar