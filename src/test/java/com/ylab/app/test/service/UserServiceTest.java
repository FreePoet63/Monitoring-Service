package com.ylab.app.test.service;

import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * UserServiceTest class
 *
 * @author razlivinsky
 * @since 27.01.2024
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDaoImpl dao;

    @Test
    @DisplayName("Register user with valid name, password and role")
    public void registerUserWithValidNamePasswordAndRole() throws SQLException {
        User user = new User("test", "123", UserRole.USER);
        when(dao.getUserByLogin(user.getName())).thenReturn(null);

        userService.registerUser(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(dao).insertUser(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getName()).isEqualTo(user.getName());
        assertThat(capturedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(capturedUser.getRole()).isEqualTo(user.getRole());
    }

    @Test
    @DisplayName("Register user with null name")
    public void registerUserWithNullName() {
        User user = new User(null, "123", UserRole.USER);

        assertThatThrownBy(() -> userService.registerUser(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");

        verifyNoInteractions(dao);
    }

    @Test
    @DisplayName("Login user with valid name and password")
    public void loginUserWithValidNameAndPassword() throws SQLException {
        User expected = new User("test", "123", UserRole.USER);
        when(dao.getUserByLogin(expected.getName())).thenReturn(expected);
        when(dao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        UserDto result = userService.getUserByLogin(expected.getName());

        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields()
                .isEqualTo(userService.getAllUsers().get(0));
    }

    @Test
    @DisplayName("Login user with non-existing name")
    public void loginUserWithNonExistingName() {
        String name = "test";

        assertThatThrownBy(() -> userService.getUserByLogin(name))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("Login user with invalid name or password")
    public void loginUserWithInvalidNameOrPassword() throws SQLException {
        User user = new User("wrong", "wrong", UserRole.USER);
        when(dao.getUserByLogin(anyString())).thenReturn(null);
        assertThrows(UserValidationException.class, () -> userService.getUserByLogin(user.getName()));
    }

    @Test
    @DisplayName("Check role for admin user")
    public void checkRoleForAdminUser() throws SQLException {
        User expected = new User("admin", "123", UserRole.ADMIN);

        when(dao.getUserByLogin(expected.getName())).thenReturn(expected);
        when(dao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        boolean result = userService.hasRoleAdmin(expected);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Check role for user")
    public void checkRoleForUser() throws SQLException {
        User expected = new User("user", "123", UserRole.USER);

        when(dao.getUserByLogin(expected.getName())).thenReturn(expected);
        when(dao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        boolean result = userService.hasRoleAdmin(expected);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Check role for null user")
    public void checkRoleForNullUser() {
        User user = null;

        assertThatThrownBy(() -> userService.hasRoleAdmin(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid user");
    }

    @Test
    @DisplayName("Get all users")
    public void getAllUsers() throws SQLException {
        User user1 = new User("user1", "123", UserRole.USER);
        User user2 = new User("user2", "456", UserRole.ADMIN);
        List<User> expected = List.of(user1, user2);

        when(dao.getAllUsers()).thenReturn(expected);
        List<UserDto> actual = userService.getAllUsers();

        assertThat(actual).isNotNull().hasSize(2);
        assertThat(actual).extracting(UserDto::getName).contains(user1.getName(), user2.getName());
    }
}