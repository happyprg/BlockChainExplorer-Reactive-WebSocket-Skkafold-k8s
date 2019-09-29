FROM openjdk:8-jdk-alpine

RUN apk add --update \
    curl \
    wget \
    net-tools \
    curl \
    git \
    && rm -rf /var/cache/apk/*
ENV springprofiles=""
COPY /build/libs/api-0.0.1-SNAPSHOT.jar api.jar
COPY ./docker/scripts/wait-for-it.sh wait-for-it.sh

ENV springprofiles="" \
    MAXRAMIFNOLIMIT=4096

ENTRYPOINT MAXRAM=$(expr `cat /sys/fs/cgroup/memory/memory.limit_in_bytes` / 1024 / 1024) && \
           MAXRAM=$(($MAXRAM>$MAXRAMIFNOLIMIT?$MAXRAMIFNOLIMIT:$MAXRAM))m && \
           echo "MaxRam: $MAXRAM" && \
           java -XX:MaxRAM=$MAXRAM -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active="$springprofiles" api.jar
EXPOSE 8080
#https://gist.github.com/kekru/e7414a9a689038b1153c48d9b2e30edf
#ENTRYPOINT ["java", "-jar", "api.jar"]