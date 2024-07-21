FROM eclipse-temurin:21-jdk

EXPOSE 1527

RUN \
    wget https://dist.apache.org/repos/dist/release/db/derby/db-derby-10.17.1.0/db-derby-10.17.1.0-bin.tar.gz -O ./db-derby-bin.tar.gz && \
    tar -xf ./db-derby-bin.tar.gz -C /opt/ && \
    rm -rf ./db-derby-bin.tar.gz

ENTRYPOINT ["java", "-jar", "/opt/db-derby-10.17.1.0-bin/lib/derbyrun.jar", "server", "start"]
