package com.ylab.app.test.util;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

/**
 * TestContainersRepository class
 *
 * @author razlivinsky
 * @since 11.02.2024
 */
public abstract class TestContainersRepository {
    public static final String POSTGRES_LATEST = new TestDataReader().readValueConfigFile("spring.datasource.version");
    public static final String DATABASE = new TestDataReader().readValueConfigFile("spring.datasource.database");
    public static final String USERNAME = new TestDataReader().readValueConfigFile("spring.datasource.username");
    public static final String PASSWORD = new TestDataReader().readValueConfigFile("spring.datasource.password");

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_LATEST)
            .withDatabaseName(DATABASE)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

}