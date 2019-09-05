FROM openjdk:8-jdk-alpine

RUN apk add --update \
    curl \
    wget \
    net-tools \
    curl \
    git \
    && rm -rf /var/cache/apk/*

EXPOSE 8080
COPY /build/libs/api-0.0.1-SNAPSHOT.jar api.jar
COPY ./docker/scripts/wait-for-it.sh wait-for-it.sh
ENTRYPOINT ["java", "-jar", "api.jar"]