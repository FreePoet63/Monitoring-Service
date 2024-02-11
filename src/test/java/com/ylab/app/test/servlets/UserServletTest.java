package com.ylab.app.test.servlets;

import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.in.servlets.UserServlet;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
/**
 * UserServletTest class
 *
 * @author HP
 * @since 11.02.2024
 */
public class UserServletTest {

    @InjectMocks
    private UserServlet userServlet;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoPost() throws Exception {
        String jsonInput = "{\"name\":\"testuser\",\"password\":\"testpassword\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("testuser", "testpassword", UserRole.USER);
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(userService.registerUser(anyString(), anyString())).thenReturn(mockedUserDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testDoPost_InvalidData_BadRequest() throws IOException, ServletException {
        String jsonInput = "{\"name\":\"testuser\",\"password\":\"\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        doThrow(new UserValidationException("Invalid credintails")).when(userService).registerUser(anyString(), anyString());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User("user1", "password1", UserRole.USER);
        User user2 = new User("user2", "password2", UserRole.USER);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto userDto1 = userMapper.userToUserDto(user1);
        UserDto userDto2= userMapper.userToUserDto(user2);
        List<UserDto> users = new ArrayList<>();
        users.add(userDto1);
        users.add(userDto2);
        when(userService.getAllUsers()).thenReturn(users);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testLoginUser_UserValidationException() throws Exception {
        when(request.getParameter("name")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("");

        doThrow(new UserValidationException("Invalid credentials")).when(userService).loginUser("testuser", "");
        userServlet.loginUser(request, response, "testuser", "");

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
    }

    @Test
    void testLoginUser() throws IOException, ServletException {
        when(request.getParameter("name")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("testpassword");

        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("testuser", "testpassword", UserRole.USER);
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(userService.loginUser("testuser", "testpassword")).thenReturn(mockedUserDto);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testGetUserById_Positive() throws Exception {
        long userId = 1;
        User user = new User("testuser", "testpassword", UserRole.USER);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto userDto = userMapper.userToUserDto(user);

        when(userService.getUserById(userId)).thenReturn(userDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testGetUserById_UserNotFound() throws Exception {
        long userId = 123;
        when(userService.getUserById(userId)).thenReturn(null);
        userServlet.getUserById(request, response, userId);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден");
    }
}