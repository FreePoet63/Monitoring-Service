package com.ylab.app.test.repository;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.ylab.app.test.util.TestDataReader.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDaoTest class
 *
 * @author razlivinskiy
 * @since 04.02.2024
 */
@Testcontainers
public class UserDaoTest {
    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            getTestDataDatabase(TEST_DATABASE_VERSION))
            .withDatabaseName(getTestDataDatabase(TEST_DATABASE))
            .withUsername(getTestDataDatabase(TEST_USER))
            .withPassword(getTestDataDatabase(TEST_PASSWORD));

    @BeforeEach
    public void setUp() throws SQLException {
        ConnectionManager manager = new ConnectionManager();
        manager.setConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
    }

    @Test
    @DisplayName("Insert user into database")
    public void testInsertUser() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        UserDaoImpl userDao = new UserDaoImpl();

        userDao.insertUser(user);

        User retrievedUser = userDao.findUserByNameAndPassword("Alice", "1234");
        assertEquals(user.getName(), retrievedUser.getName());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        assertEquals(user.getRole(), retrievedUser.getRole());
    }

    @Test
    @DisplayName("Find user by name and password with wrong credentials")
    public void testFindUserByNameAndPasswordWithWrongCredentials() throws SQLException {
        String wrongName = "Bob";
        String wrongPassword = "4321";
        UserDaoImpl userDao = new UserDaoImpl();
        User result = userDao.findUserByNameAndPassword(wrongName, wrongPassword);
        assertNull(result);
    }

    @Test
    @DisplayName("Find user by name and password with valid credentials")
    public void testFindUserByNameAndPasswordWithValidCredentials() throws SQLException {
        String validName = "Alice";
        String validPassword = "1234";
        UserRole validRole = UserRole.USER;
        UserDaoImpl userDao = new UserDaoImpl();

        User expectedUser = new User(validName, validPassword, validRole);
        userDao.insertUser(expectedUser);

        User result = userDao.findUserByNameAndPassword(validName, validPassword);

        assertNotNull(result);
        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getPassword(), result.getPassword());
        assertEquals(expectedUser.getRole(), result.getRole());
    }

    @Test
    @DisplayName("Get all users with non-empty database")
    public void testGetAllUsersWithNonEmptyDatabase() throws SQLException {
        User user1 = new User("Alice", "1234", UserRole.USER);
        User user2 = new User("Bob", "4321", UserRole.ADMIN);
        UserDaoImpl userDao = new UserDaoImpl();

        userDao.insertUser(user1);
        userDao.insertUser(user2);

        List<User> result = userDao.getAllUsers();

        assertFalse(result.isEmpty());
        assertThat(result)
                .isNotEmpty()
                .extracting(User::getName, User::getPassword, User::getRole)
                .containsAnyOf(
                        tuple("Alice", "1234", UserRole.USER),
                        tuple("Bob", "4321", UserRole.ADMIN)
                );
    }
}
