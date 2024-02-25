package com.ylab.app.test.service;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.in.StartApplication;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * UserServiceTest class
 *
 * @author razlivinsky
 * @since 27.01.2024
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = StartApplication.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Register user with valid name, password and role")
    void registerUser_success() throws SQLException {
        User user = new User("test", "password", UserRole.USER);
        UserDto userDto = userMapper.userToUserDto(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        doNothing().when(userDao).insertUser(any(User.class));
        when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);
        UserDto result = userService.registerUser(user);
        System.out.println("Result from registerUser: " + result);
        assertThat(result).isEqualTo(userDto);
        verify(userDao, times(1)).insertUser(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    @DisplayName("Register user with null name")
    public void registerUserWithNullName() {
        User user = new User(null, "123", UserRole.USER);

        assertThatThrownBy(() -> userService.registerUser(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");

        verifyNoInteractions(userDao);
    }

    @Test
    @DisplayName("Login user with valid name and password")
    public void loginUserWithValidNameAndPassword() throws SQLException {
        User expected = new User("test", "123", UserRole.USER);
        when(userDao.getUserByLogin(expected.getUsername())).thenReturn(expected);
        when(userDao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        UserDto result = userService.getUserByLogin(expected.getUsername());
        System.out.println(result);

        assertThat(result)
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
        when(userDao.getUserByLogin(anyString())).thenReturn(null);
        assertThrows(UserValidationException.class, () -> userService.getUserByLogin(user.getUsername()));
    }

    @Test
    @DisplayName("Check role for admin user")
    public void checkRoleForAdminUser() throws SQLException {
        User expected = new User("admin", "123", UserRole.ADMIN);

        when(userDao.getUserByLogin(expected.getUsername())).thenReturn(expected);
        when(userDao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        boolean result = userService.hasRoleAdmin(expected);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Check role for user")
    public void checkRoleForUser() throws SQLException {
        User expected = new User("user", "123", UserRole.USER);

        when(userDao.getUserByLogin(expected.getUsername())).thenReturn(expected);
        when(userDao.getAllUsers()).thenReturn(Collections.singletonList(expected));

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
        User user1 = new User("admin", "admin", UserRole.ADMIN);
        User user2 = new User("test", "test", UserRole.USER);
        UserDto userDto1 = userMapper.userToUserDto(user1);
        UserDto userDto2 = userMapper.userToUserDto(user2);
        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        given(userDao.getAllUsers()).willReturn(users);
        given(userMapper.userToUserDto(any(User.class))).willReturn(userDto1, userDto2);

        List<UserDto> result = userService.getAllUsers();
        assertThat(result).isEqualTo(userDtos);
    }
}