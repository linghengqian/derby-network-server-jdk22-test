package com.lingh;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class DerbyTest {

    @Test
    void testEmbeddedDerby() {
        assertDoesNotThrow(() -> {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
            hikariConfig.setJdbcUrl("jdbc:derby:memory:config;create=true");
            try (HikariDataSource dataSource = new HikariDataSource(hikariConfig);
                 Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute("""
                        CREATE TABLE repository(id varchar(36) PRIMARY KEY, "key" varchar(32672), value varchar(32672), parent varchar(32672))
                        """);
                statement.execute("INSERT INTO repository VALUES('test1', 'test1', 'test1', 'test1')");
                statement.execute("""
                        UPDATE repository SET value = 'test2' WHERE "key" = 'test1'
                        """);
                statement.execute("""
                        SELECT value FROM repository WHERE "key" = 'test1'
                        """);
                statement.execute("""
                        SELECT DISTINCT("key") FROM repository WHERE parent = 'test1'
                        """);
                statement.execute("""
                        DELETE FROM repository WHERE "key" LIKE 'test1'
                        """);
            }
        });
    }

    @SuppressWarnings("resource")
    @Test
    @Disabled
    void testDerbyNetworkServer() {
        try (GenericContainer<?> container = new GenericContainer<>(
                new ImageFromDockerfile().withFileFromClasspath("Dockerfile", "derby-network-server/Dockerfile"))
                .withExposedPorts(1527)) {
            container.start();
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
            hikariConfig.setJdbcUrl("jdbc:derby://localhost:" + container.getMappedPort(1527) + "/MyDbTest;create=true");
            Awaitility.await().atMost(Duration.ofSeconds(5L)).ignoreExceptions().until(() -> {
                new HikariDataSource(hikariConfig).getConnection().close();
                return true;
            });
        }

    }
}
