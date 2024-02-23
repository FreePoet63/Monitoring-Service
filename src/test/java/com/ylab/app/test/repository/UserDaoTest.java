package com.ylab.app.test.repository;

import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.test.util.DataSourceConfig;
import com.ylab.app.test.util.TestContainersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

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
public class UserDaoTest extends TestContainersRepository {
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        DataSourceConfig testDataSourceConfig = new DataSourceConfig();
        DataSource dataSource = testDataSourceConfig.dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @DisplayName("Insert user into database")
    public void testInsertUser() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        UserDaoImpl userDao = new UserDaoImpl(jdbcTemplate);

        userDao.insertUser(user);

        User retrievedUser = userDao.getUserByLogin(user.getName());
        assertEquals(user.getName(), retrievedUser.getName());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        assertEquals(user.getRole(), retrievedUser.getRole());
    }

    @Test
    @DisplayName("Find user by name and password with wrong credentials")
    public void testFindUserByNameAndPasswordWithWrongCredentials() throws SQLException {
        User user = new User("Bob", "4231", UserRole.USER);
        UserDaoImpl userDao = new UserDaoImpl(jdbcTemplate);
        User result = userDao.getUserByLogin(user.getName());
        assertNull(result);
    }

    @Test
    @DisplayName("Find user by name and password with valid credentials")
    public void testFindUserByNameAndPasswordWithValidCredentials() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        UserDaoImpl userDao = new UserDaoImpl(jdbcTemplate);

        userDao.insertUser(user);
        User result = userDao.getUserByLogin(user.getName());

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    @DisplayName("Get all users with non-empty database")
    public void testGetAllUsersWithNonEmptyDatabase() throws SQLException {
        User user1 = new User("Alice", "1234", UserRole.USER);
        User user2 = new User("Bob", "4321", UserRole.ADMIN);
        UserDaoImpl userDao = new UserDaoImpl(jdbcTemplate);

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
