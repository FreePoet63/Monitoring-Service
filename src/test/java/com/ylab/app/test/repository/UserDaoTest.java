package com.ylab.app.test.repository;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.test.util.TestContainersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * UserDaoTest class
 *
 * @author razlivinskiy
 * @since 04.02.2024
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
public class UserDaoTest extends TestContainersRepository {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserDao userDao = new UserDaoImpl(jdbcTemplate);

    private User user1;
    private User user2;
    private List<User> userList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        user1 = new User("alice", "12345", UserRole.USER);
        user1.setId(1L);
        user2 = new User("bob", "67890", UserRole.ADMIN);
        user2.setId(2L);
        userList = Arrays.asList(user1, user2);
    }

    @Test
    @DisplayName("insertUser inserts a new user into the database when successful")
    public void insertUser_InsertNewUser_WhenSuccessful() throws SQLException {
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenReturn(1);
        userDao.insertUser(user1);

        assertThat(user1.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("insertUser throws DatabaseWriteException when insertion fails")
    public void insertUser_ThrowDatabaseWriteException_WhenInsertionFails() {
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenThrow(DataAccessException.class);

        assertThatThrownBy(() -> userDao.insertUser(user1))
                .isInstanceOf(DatabaseWriteException.class)
                .hasMessage("Failed to insert user");
    }

    @Test
    @DisplayName("getAllUsers returns a list of all users from the database when successful")
    public void getAllUsers_ReturnListOfAllUsers_WhenSuccessful() throws SQLException {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(userList);
        List<User> result = userDao.getAllUsers();

        assertThat(result).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    @DisplayName("getAllUsers throws DatabaseReadException when retrieval fails")
    public void getAllUsers_ThrowDatabaseReadException_WhenRetrievalFails() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenThrow(DataAccessException.class);

        assertThatThrownBy(() -> userDao.getAllUsers())
                .isInstanceOf(DatabaseReadException.class)
                .hasMessage("Failed to retrieve all users");
    }

    @Test
    @DisplayName("findUserById returns a user with the specified ID from the database when successful")
    public void findUserById_ReturnUserWithSpecifiedId_WhenSuccessful() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong())).thenReturn(user1);
        User result = userDao.findUserById(1L);

        assertThat(result).isEqualTo(user1);
    }

    @Test
    @DisplayName("findUserById throws UserValidationException when user with the given ID is not found")
    public void findUserById_ThrowUserValidationException_WhenUserNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong())).thenThrow(EmptyResultDataAccessException.class);

        assertThatThrownBy(() -> userDao.findUserById(1L))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("user not found");
    }

    @Test
    @DisplayName("findUserById throws DatabaseReadException when retrieval fails")
    public void findUserById_ThrowDatabaseReadException_WhenRetrievalFails() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong())).thenThrow(DataAccessException.class);

        assertThatThrownBy(() -> userDao.findUserById(1L))
                .isInstanceOf(DatabaseReadException.class)
                .hasMessage("Failed to retrieve user by id");
    }

    @Test
    @DisplayName("getUserByLogin returns a user with the specified login name from the database when successful")
    public void getUserByLogin_ReturnUserWithSpecifiedLogin_WhenSuccessful() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString())).thenReturn(user1);
        User result = userDao.getUserByLogin("alice");

        assertThat(result).isEqualTo(user1);
    }
}
