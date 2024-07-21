# derby-network-server-jdk22-test

- For https://issues.apache.org/jira/browse/DERBY-7165 and https://issues.apache.org/jira/browse/DERBY-7166 .
- Execute the following command on the Ubuntu 22.04.3 instance with `SDKMAN!` and `Docker CE` installed.

# 1st

- Manually build and test the Docker Image of the Derby Network Server.
  This is not necessary to execute unit tests through maven commands.
  In unit tests, the current project builds the Docker Image through testcontainers-java.

```shell
docker build -f ./src/test/resources/Dockerfile --tag apache/derby-network-server:latest .
docker tag apache/derby-network-server:latest apache/derby-network-server:10.17.1.0
docker run -p 11527:1527 --rm=true apache/derby-network-server:10.17.1.0
```

# 2nd

- Execute unit tests.

```shell
sdk install java 22.0.2-graalce
sdk use java 22.0.2-graalce

git clone git@github.com:linghengqian/derby-network-server-jdk22-test.git
cd ./derby-network-server-jdk22-test/
./mvnw -T1C -e clean test
```