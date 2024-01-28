package com.ylab.app.test.service;

import com.ylab.app.model.*;
import com.ylab.app.service.*;
import com.ylab.app.service.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.mockito.quality.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserServiceTest class
 *
 * @author HP
 * @since 27.01.2024
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    private UserService userService;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    @DisplayName("Register user with valid name, password and role")
    public void registerUserWithValidNamePasswordAndRole() {
        String name = "test";
        String password = "123";
        String role = "user";

        userService.registerUser(name, password, role);

        assertThat(userService.getAllUsers())
                .hasSize(1)
                .first()
                .extracting(User::getName, User::getPassword, User::getRole)
                .containsExactly(name, password, role);
    }

    @Test
    @DisplayName("Register user with null name")
    public void registerUserWithNullName() {
        String name = null;
        String password = "123";
        String role = "user";

        assertThatThrownBy(() -> userService.registerUser(name, password, role))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name");
    }

    @Test
    @DisplayName("Register user with existing name")
    public void registerUserWithExistingName() {
        String name = "test";
        String password = "123";
        String role = "user";
        userService.registerUser(name, password, role);

        assertThatThrownBy(() -> userService.registerUser(name, password, role))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User already exists");
    }

    @Test
    @DisplayName("Login user with valid name and password")
    public void loginUserWithValidNameAndPassword() {
        String name = "test";
        String password = "123";
        String role = "user";
        userService.registerUser(name, password, role);

        User result = userService.loginUser(name, password);

        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields()
                .isEqualTo(userService.getAllUsers().get(0));
    }

    @Test
    @DisplayName("Login user with null name")
    public void loginUserWithNullName() {
        String name = null;
        String password = "123";

        assertThatThrownBy(() -> userService.loginUser(name, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name");
    }

    @Test
    @DisplayName("Login user with non-existing name")
    public void loginUserWithNonExistingName() {
        String name = "test";
        String password = "123";

        assertThatThrownBy(() -> userService.loginUser(name, password))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User does not exist");
    }

    @Test
    @DisplayName("Login user with wrong password")
    public void loginUserWithWrongPassword() {
        String name = "test";
        String password = "123";
        String role = "user";
        userService.registerUser(name, password, role);

        assertThatThrownBy(() -> userService.loginUser(name, "456"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Wrong password");
    }

    @Test
    @DisplayName("Check role for admin user")
    public void checkRoleForAdminUser() {
        String name = "admin";
        String password = "123";
        String role = "admin";
        userService.registerUser(name, password, role);
        when(user.getName()).thenReturn(name);
        when(user.getPassword()).thenReturn(password);
        when(user.getRole()).thenReturn(role);

        boolean result = userService.checkRole(user);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Check role for user user")
    public void checkRoleForUserUser() {
        String name = "user";
        String password = "123";
        String role = "user";
        userService.registerUser(name, password, role);
        when(user.getName()).thenReturn(name);
        when(user.getPassword()).thenReturn(password);
        when(user.getRole()).thenReturn(role);

        boolean result = userService.checkRole(user);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Check role for null user")
    public void checkRoleForNullUser() {
        User user = null;

        assertThatThrownBy(() -> userService.checkRole(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid user");
    }

    @Test
    @DisplayName("Get all users")
    public void getAllUsers() {
        String name1 = "user1";
        String password1 = "123";
        String role1 = "user";
        userService.registerUser(name1, password1, role1);
        String name2 = "user2";
        String password2 = "456";
        String role2 = "admin";
        userService.registerUser(name2, password2, role2);

        List<User> result = userService.getAllUsers();

        assertThat(result).isNotNull()
                .hasSize(2)
                .extracting(User::getName, User::getPassword, User::getRole)
                .containsExactlyInAnyOrder(
                        tuple(name1, password1, role1),
                        tuple(name2, password2, role2)
                );
    }
}