# derby-network-server-jdk22-test

- For https://issues.apache.org/jira/browse/DERBY-7165 , https://issues.apache.org/jira/browse/DERBY-7108 and https://issues.apache.org/jira/browse/DERBY-7166 .
- Execute the following command on the Ubuntu 22.04.3 instance with `SDKMAN!` and `Docker CE` installed.

# 1st

- Manually build and test the Docker Image of the Derby Network Server.
  This is not necessary to execute unit tests through maven commands.
  In unit tests, the current project builds the Docker Image through testcontainers-java.

```shell
docker build -f ./src/test/resources/derby-network-server/Dockerfile --tag apache/derby-network-server:latest .
docker tag apache/derby-network-server:latest apache/derby-network-server:10.17.1.0
docker run -p 11527:1527 --rm=true apache/derby-network-server:10.17.1.0
```

# 2nd

- Execute unit tests under the JVM.

```shell
sdk install java 22.0.2-graalce
sdk use java 22.0.2-graalce

git clone git@github.com:linghengqian/derby-network-server-jdk22-test.git
cd ./derby-network-server-jdk22-test/
./mvnw -T1C -e clean test
```

# optional, 3rd
- Execute unit tests under GraalVM Native Image.
```shell
sdk install java 22.0.2-graalce
sdk use java 22.0.2-graalce
sudo apt-get install build-essential zlib1g-dev -y

git clone git@github.com:linghengqian/derby-network-server-jdk22-test.git
cd ./derby-network-server-jdk22-test/

# Running `./mvnw -PgenerateMetadata -DskipNativeTests -e -T1C clean test native:metadata-copy` is not necessary unless unit tests require more GRM
# ./mvnw -PgenerateMetadata -DskipNativeTests -e -T1C clean test native:metadata-copy

# Running unit tests under GraalVM Native Image
./mvnw -PnativeTestInJunit -T1C -e clean test
```

- Log.
```shell
JUnit Platform on Native Image - report
----------------------------------------

14:04:19.511 [main] DEBUG com.zaxxer.hikari.HikariConfig - Driver class org.apache.derby.jdbc.EmbeddedDriver not found in Thread context class loader jdk.internal.loader.ClassLoaders$AppClassLoader@e580929, trying classloader jdk.internal.loader.ClassLoaders$AppClassLoader@e580929
14:04:19.511 [main] ERROR com.zaxxer.hikari.HikariConfig - Failed to load driver class org.apache.derby.jdbc.EmbeddedDriver from HikariConfig class classloader jdk.internal.loader.ClassLoaders$AppClassLoader@e580929
com.lingh.DerbyTest > testDerbyNetworkServer() SKIPPED: void com.lingh.DerbyTest.testDerbyNetworkServer() is @Disabled

com.lingh.DerbyTest > testEmbeddedDerby() FAILED


Failures (1):
  JUnit Jupiter:DerbyTest:testEmbeddedDerby()
    MethodSource [className = 'com.lingh.DerbyTest', methodName = 'testEmbeddedDerby', methodParameterTypes = '']
    => org.opentest4j.AssertionFailedError: Unexpected exception thrown: java.lang.RuntimeException: Failed to load driver class org.apache.derby.jdbc.EmbeddedDriver in either of HikariConfig class loader or Thread context classloader
       org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:152)
       org.junit.jupiter.api.AssertDoesNotThrow.createAssertionFailedError(AssertDoesNotThrow.java:84)
       org.junit.jupiter.api.AssertDoesNotThrow.assertDoesNotThrow(AssertDoesNotThrow.java:53)
       org.junit.jupiter.api.AssertDoesNotThrow.assertDoesNotThrow(AssertDoesNotThrow.java:36)
       org.junit.jupiter.api.Assertions.assertDoesNotThrow(Assertions.java:3168)
       [...]
     Caused by: java.lang.RuntimeException: Failed to load driver class org.apache.derby.jdbc.EmbeddedDriver in either of HikariConfig class loader or Thread context classloader
       com.zaxxer.hikari.HikariConfig.setDriverClassName(HikariConfig.java:494)
       com.lingh.DerbyTest.lambda$testEmbeddedDerby$0(DerbyTest.java:24)
       org.junit.jupiter.api.AssertDoesNotThrow.assertDoesNotThrow(AssertDoesNotThrow.java:49)
       [...]

Test run finished after 5 ms
[         2 containers found      ]
[         0 containers skipped    ]
[         2 containers started    ]
[         0 containers aborted    ]
[         2 containers successful ]
[         0 containers failed     ]
[         2 tests found           ]
[         1 tests skipped         ]
[         1 tests started         ]
[         0 tests aborted         ]
[         0 tests successful      ]
[         1 tests failed          ]


```