package com.ylab.app.test.util;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static com.ylab.app.test.util.TestDataReader.*;

/**
 * TestContainersRepository class
 *
 * @author HP
 * @since 11.02.2024
 */
public abstract class TestContainersRepository {
    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            getTestDataDatabase(TEST_DATABASE_VERSION))
            .withDatabaseName(getTestDataDatabase(TEST_DATABASE))
            .withUsername(getTestDataDatabase(TEST_USER))
            .withPassword(getTestDataDatabase(TEST_PASSWORD));
}