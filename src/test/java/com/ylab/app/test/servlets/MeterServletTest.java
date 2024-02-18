package com.ylab.app.test.servlets;

/**
 * MeterServletTest class
 *
 * @author HP
 * @since 11.02.2024
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.app.exception.meterException.MeterReadingException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.in.servlets.MeterServlet;
import com.ylab.app.mapper.MeterReadingMapper;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.model.dto.MeterReadingRequestDto;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.MeterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class MeterServletTest {

    @InjectMocks
    private MeterServlet meterServlet;

    @Mock
    private MeterService meterService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoPost_Success() throws Exception {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();
        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("testuser", "testpassword", UserRole.USER);
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        MeterReadingMapper readingMapper = MeterReadingMapper.INSTANCE;
        User expectedUser = new User("test", "123", UserRole.USER);
        MeterReading meterReading = new MeterReading("123", LocalDateTime.now(), expectedUser);
        meterReading.addReadingDetails("electricity", 100.0);
        MeterReadingDto meterReadingDto = readingMapper.meterReadingToMeterReadingDto(meterReading);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        BufferedReader reader = mock(BufferedReader.class);
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn("{ \"detailsList\": [{\"type\": \"\", \"value\": 100.0}] }", null);
        when(meterService.submitReading(any(User.class), eq("meter123"), anyList())).thenReturn(meterReadingDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        meterServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }


    @Test
    public void testGetCurrentReadings() throws Exception {
        MeterReadingMapper readingMapper = MeterReadingMapper.INSTANCE;
        User expectedUser = new User("test", "123", UserRole.USER);
        MeterReading meterReading = new MeterReading("123", LocalDateTime.now(), expectedUser);
        meterReading.addReadingDetails("gas", 10.0);
        MeterReadingDto meterReadingDto = readingMapper.meterReadingToMeterReadingDto(meterReading);
        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("testuser", "testpassword", UserRole.USER);
        UserDto mockedUserDto = userMapper.userToUserDto(user);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);

        when(request.getParameter("action")).thenReturn("current");
        when(meterService.getCurrentReadings(Mockito.any(User.class)))
                .thenReturn(Arrays.asList(meterReadingDto));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        meterServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }


    @Test
    public void testGetReadingsByMonth() throws Exception {
        when(request.getParameter("action")).thenReturn("month");
        when(request.getParameter("month")).thenReturn("2");
        MeterReadingMapper readingMapper = MeterReadingMapper.INSTANCE;
        User expectedUser = new User("test", "123", UserRole.USER);
        MeterReading meterReading = new MeterReading("123", LocalDateTime.now(), expectedUser);
        meterReading.addReadingDetails("gas", 10.0);
        MeterReadingDto meterReadingDto = readingMapper.meterReadingToMeterReadingDto(meterReading);
        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("testuser", "testpassword", UserRole.USER);
        UserDto mockedUserDto = userMapper.userToUserDto(user);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        when(meterService.getReadingsByMonth(Mockito.any(User.class), Mockito.anyInt()))
                .thenReturn(Arrays.asList(meterReadingDto));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        meterServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testGetReadingsHistory() throws Exception {
        when(request.getParameter("action")).thenReturn("history");
        MeterReadingMapper readingMapper = MeterReadingMapper.INSTANCE;
        User expectedUser = new User("test", "123", UserRole.USER);
        MeterReading meterReading = new MeterReading("123", LocalDateTime.now(), expectedUser);
        meterReading.addReadingDetails("gas", 10.0);
        MeterReadingDto meterReadingDto = readingMapper.meterReadingToMeterReadingDto(meterReading);
        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("testuser", "testpassword", UserRole.USER);
        UserDto mockedUserDto = userMapper.userToUserDto(user);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        when(meterService.getReadingsHistory(Mockito.any(User.class)))
                .thenReturn(Arrays.asList(meterReadingDto));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        meterServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testGetReadingsAllHistory() throws Exception {
        when(request.getParameter("action")).thenReturn("all");
        MeterReadingMapper readingMapper = MeterReadingMapper.INSTANCE;
        User expectedUser = new User("test", "123", UserRole.ADMIN);
        MeterReading meterReading = new MeterReading("123", LocalDateTime.now(), expectedUser);
        meterReading.addReadingDetails("gas", 10.0);
        MeterReadingDto meterReadingDto = readingMapper.meterReadingToMeterReadingDto(meterReading);
        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("testuser", "testpassword", UserRole.ADMIN);
        UserDto mockedUserDto = userMapper.userToUserDto(user);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        when(meterService.getAllReadingsHistory(Mockito.any(User.class)))
                .thenReturn(Arrays.asList(meterReadingDto));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        meterServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testGetReadingsByMonth_UserValidationException() throws Exception {
        User user = new User("test", "123", UserRole.USER);
        when(request.getParameter("month")).thenReturn("5");
        when(meterService.getReadingsByMonth(user, 5)).thenThrow(new UserValidationException("Invalid user"));

        meterServlet.getReadingsByMonth(request, response, user);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user");
    }

    @Test
    void testGetAllReadingsHistory_UserValidationException() throws Exception {
        User user = new User("test", "123", UserRole.USER);
        when(meterService.getAllReadingsHistory(user)).thenThrow(new UserValidationException("Invalid user"));

        meterServlet.getAllReadingsHistory(mock(HttpServletRequest.class), response, user);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid user");
    }
}
