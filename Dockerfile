# Build stage
ARG APP_VERSION="2.1.2"

FROM docker.io/gradle:9-jdk25 AS builder
ARG APP_VERSION
WORKDIR /build
COPY . .
RUN gradle build -x test

# Runtime stage
FROM registry:5000/awscorretto:27Hold
ARG APP_VERSION="2.1.2"
COPY --from=builder /build/build/libs/traffic-mon-${APP_VERSION}.jar /app/traffic-mon.jar
WORKDIR /app
USER nobody
ENTRYPOINT ["java", "-jar", "traffic-mon.jar"]
