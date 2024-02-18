package com.ylab.app.test.service;

import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.mapper.UserMapper;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * UserServiceTest class
 *
 * @author HP
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
        String name = "test";
        String password = "123";
        UserRole role = UserRole.USER;

        when(dao.findUserByNameAndPassword(name, password)).thenReturn(null);

        userService.registerUser(name, password);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(dao).insertUser(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getName()).isEqualTo(name);
        assertThat(capturedUser.getPassword()).isEqualTo(password);
        assertThat(capturedUser.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("Register user with null name")
    public void registerUserWithNullName() {
        String name = null;
        String password = "123";
        UserRole role = UserRole.USER;

        assertThatThrownBy(() -> userService.registerUser(name, password))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");

        verifyNoInteractions(dao);
    }

    @Test
    @DisplayName("Login user with valid name and password")
    public void loginUserWithValidNameAndPassword() throws SQLException {
        String name = "test";
        String password = "123";
        UserRole role = UserRole.USER;
        User expected = new User(name, password, role);

        when(dao.findUserByNameAndPassword(name, password)).thenReturn(expected);
        when(dao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        UserDto result = userService.loginUser(name, password);

        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields()
                .isEqualTo(userService.getAllUsers().get(0));
    }

    @Test
    @DisplayName("Login user with non-existing name")
    public void loginUserWithNonExistingName() {
        String name = "test";
        String password = "123";

        assertThatThrownBy(() -> userService.loginUser(name, password))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("Login user with invalid name or password")
    public void loginUserWithInvalidNameOrPassword() throws SQLException {
        String name = "test";
        String password = "123";
        String wrongName = "wrong";
        String wrongPassword = "wrong";

        when(dao.findUserByNameAndPassword(anyString(), anyString())).thenReturn(null);

        assertThrows(UserValidationException.class, () -> userService.loginUser(wrongName, password));
        assertThrows(UserValidationException.class, () -> userService.loginUser(name, wrongPassword));
    }

    @Test
    @DisplayName("Check role for admin user")
    public void checkRoleForAdminUser() throws SQLException {
        String name = "admin";
        String password = "123";
        UserRole role = UserRole.ADMIN;
        User expected = new User(name, password, role);

        when(dao.findUserByNameAndPassword(name, password)).thenReturn(expected);
        when(dao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        boolean result = userService.hasRoleAdmin(expected);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Check role for user user")
    public void checkRoleForUserUser() throws SQLException {
        String name = "user";
        String password = "123";
        UserRole role = UserRole.USER;
        User expected = new User(name, password, role);

        when(dao.findUserByNameAndPassword(name, password)).thenReturn(expected);
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