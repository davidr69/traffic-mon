FROM pi4apps:5000/awscorretto:25

#ARG APP_NAME
#ARG APP_VERSION

COPY build/libs/traffic-mon-2.0.3.jar /app/

WORKDIR /app
USER nobody

ENTRYPOINT ["/bin/sh", "-c", "java -Dspring.profiles.active=$profile -Dspring.config.additional-location=$additional_properties -jar traffic-mon-2.0.3.jar"]
