package com.ylab.app.test.controllers;

/**
 * UserControllerTest class
 *
 * @author HP
 * @since 18.02.2024
 */

import com.ylab.app.in.controllers.UserController;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Testing user registration endpoint")
    public void testRegisterUserEndpoint() throws Exception {
        User user = new User("testUser", "testUser", UserRole.USER);
        UserDto userDto = userMapper.userToUserDto(user);
        when(userService.registerUser(user)).thenReturn(userDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testUser\", \"password\": \"testUser\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("Testing get user by ID endpoint")
    public void testGetUserByIdEndpoint() throws Exception {
        User user = new User("testUser", "testUser", UserRole.USER);
        UserDto userDto = userMapper.userToUserDto(user);
        when(userService.getUserById(1L)).thenReturn(userDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("Testing get all users endpoint")
    public void testGetAllUsersEndpoint() throws Exception {
        User user1 = new User("testUser", "testUser", UserRole.USER);
        UserDto userDto1 = userMapper.userToUserDto(user1);
        User user2 = new User("testUser", "testUser", UserRole.USER);
        UserDto userDto2 = userMapper.userToUserDto(user2);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDto1, userDto2));
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }
}
