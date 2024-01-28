package com.ylab.app.test.service;

import com.ylab.app.model.*;
import com.ylab.app.service.*;
import com.ylab.app.service.impl.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.io.*;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * WebServiceTest class
 *
 * @author razlivinsky
 * @since 28.01.2024
 */
@ExtendWith(MockitoExtension.class)
public class WebServiceTest {
    private WebService webService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private MeterServiceImpl meterService;

    @Mock
    private User user;

    @Mock
    private MeterReading meterReading;

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    public void setUp() {
        webService = new WebServiceImpl(userService, meterService);
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @DisplayName("Handle register request with valid data")
    public void handleRegisterRequestWithValidData() {
        String name = "user1";
        String password = "pass1";
        String role = "user";

        webService.handleRegisterRequest(name, password, role);

        Mockito.verify(userService).registerUser(name, password, role);
        Assertions.assertThat(outContent.toString()).contains("User registered successfully");
    }

    @Test
    @DisplayName("Handle register request with invalid data")
    public void handleRegisterRequestWithInvalidData() {
        String name = "user1";
        String password = "pass1";
        String role = "user";
        Mockito.doThrow(new IllegalArgumentException("Invalid data")).when(userService).registerUser(name, password, role);

        webService.handleRegisterRequest(name, password, role);

        Mockito.verify(userService).registerUser(name, password, role);
        Assertions.assertThat(outContent.toString()).contains("Error: Invalid data");
    }

    @Test
    @DisplayName("Handle login request with valid data")
    public void handleLoginRequestWithValidData() {
        String name = "user1";
        String password = "pass1";
        Mockito.when(userService.loginUser(name, password)).thenReturn(user);

        User result = webService.handleLoginRequest(name, password);

        Mockito.verify(userService).loginUser(name, password);
        Assertions.assertThat(result).isEqualTo(user);
        Assertions.assertThat(outContent.toString()).contains("User logged in successfully");
    }

    @Test
    @DisplayName("Handle login request with invalid data")
    public void handleLoginRequestWithInvalidData() {
        String name = "user1";
        String password = "pass1";
        Mockito.doThrow(new IllegalArgumentException("Invalid data")).when(userService).loginUser(name, password);

        User result = webService.handleLoginRequest(name, password);

        Mockito.verify(userService).loginUser(name, password);
        Assertions.assertThat(result).isNull();
        Assertions.assertThat(outContent.toString()).contains("Error: Invalid data");
    }

    @Test
    @DisplayName("Handle get current readings request with valid user")
    public void handleGetCurrentReadingsRequestWithValidUser() {
        Mockito.when(meterService.getCurrentReadings(user)).thenReturn(List.of(meterReading));
        Mockito.when(user.getName()).thenReturn("user1");
        Mockito.when(meterReading.toString()).thenReturn("MeterReading{numberMeter='123', date=2024-02-15, userName='user1', readings={gas=15.0, water=25.0}}");

        webService.handleGetCurrentReadingsRequest(user);

        Mockito.verify(meterService).getCurrentReadings(user);
        Assertions.assertThat(outContent.toString()).contains("user1 1 current readings");
        Assertions.assertThat(outContent.toString())
                .contains("MeterReading{numberMeter='123', date=2024-02-15, userName='user1', readings={gas=15.0, water=25.0}}");
    }

    @Test
    @DisplayName("Handle get current readings request with null user")
    public void handleGetCurrentReadingsRequestWithNullUser() {
        User user = null;
        webService.handleGetCurrentReadingsRequest(user);
        assertThat(outContent.toString(), containsString("\"user\" is null"));
    }

    @Test
    @DisplayName("Handle submit reading request with valid data")
    public void handleSubmitReadingRequestWithValidData() {
        String numberMeter = "123";
        String type = "gas";
        double value = 10.0;

        User user = new User("user1", "111", "user");

        webService.handleSubmitReadingRequest(user, numberMeter, type, value);

        Mockito.verify(meterService).submitReading(user, numberMeter, type, value);
        Assertions.assertThat(outContent.toString()).contains("Reading submitted successfully");
    }

    @Test
    @DisplayName("Handle submit reading request with invalid data")
    public void handleSubmitReadingRequestWithInvalidData() {
        String numberMeter = "123";
        String type = "gas";
        double value = 10.0;
        Mockito.doThrow(new IllegalArgumentException("Invalid data")).when(meterService).submitReading(user, numberMeter, type, value);

        webService.handleSubmitReadingRequest(user, numberMeter, type, value);

        Mockito.verify(meterService).submitReading(user, numberMeter, type, value);
        Assertions.assertThat(outContent.toString()).contains("Error: Invalid data");
    }

    @Test
    @DisplayName("Handle get readings by month request with valid user and month")
    public void handleGetReadingsByMonthRequestWithValidUserAndMonth() {
        int month = 1;
        Mockito.when(meterService.getReadingsByMonth(user, month)).thenReturn(List.of(meterReading));
        Mockito.when(user.getName()).thenReturn("user1");
        Mockito.when(meterReading.toString()).thenReturn("MeterReading{numberMeter='123', date=2024-01-15, userName='user1', readings={gas=10.0, water=20.0}}");

        webService.handleGetReadingsByMonthRequest(user, month);

        Mockito.verify(meterService).getReadingsByMonth(user, month);
        Assertions.assertThat(outContent.toString()).contains("user1 1 readings for month 1");
        Assertions.assertThat(outContent.toString())
                .contains("MeterReading{numberMeter='123', date=2024-01-15, userName='user1', readings={gas=10.0, water=20.0}}");
    }

    @Test
    @DisplayName("Handle get readings by month request with null user")
    public void handleGetReadingsByMonthRequestWithNullUser() {
        User user = null;
        int month = 1;
        webService.handleGetReadingsByMonthRequest(user, month);
        assertThat(outContent.toString(), containsString("\"user\" is null"));
    }


    @Test
    @DisplayName("Handle get readings history request with valid user")
    public void handleGetReadingsHistoryRequestWithValidUser() {
        Mockito.when(meterService.getReadingsHistory(user)).thenReturn(List.of(meterReading, meterReading));
        Mockito.when(user.getName()).thenReturn("user1");
        Mockito.when(meterReading.toString()).thenReturn("MeterReading{numberMeter='123', date=2024-01-15, userName='user1', readings={gas=10.0, water=20.0}}");

        webService.handleGetReadingsHistoryRequest(user);

        Mockito.verify(meterService).getReadingsHistory(user);
        Assertions.assertThat(outContent.toString()).contains("user1 2 readings in history");
        Assertions.assertThat(outContent.toString())
                .contains("MeterReading{numberMeter='123', date=2024-01-15, userName='user1', readings={gas=10.0, water=20.0}}");
    }

    @Test
    @DisplayName("Handle get readings history request with null user")
    public void handleGetReadingsHistoryRequestWithNullUser() {
        User user = null;
        webService.handleGetReadingsHistoryRequest(user);
        assertThat(outContent.toString(), containsString("\"user\" is null"));
    }

    @Test
    @DisplayName("Handle get all readings request with valid admin user")
    public void handleGetAllReadingsRequestWithValidAdminUser() {
        Mockito.when(userService.checkRole(user)).thenReturn(true);
        Mockito.when(meterService.getAllReadingsHistory(user)).thenReturn(List.of(meterReading, meterReading, meterReading, meterReading));
        Mockito.when(meterReading.toString()).thenReturn("MeterReading{numberMeter='123', date=2024-01-15, userName='user1', readings={gas=10.0, water=20.0}}");

        webService.handleGetAllReadingsRequest(user);

        Mockito.verify(userService).checkRole(user);

        Mockito.verify(meterService).getAllReadingsHistory(user);
        Assertions.assertThat(outContent.toString()).contains("All readings across all users:");
        Assertions.assertThat(outContent.toString())
                .contains("MeterReading{numberMeter='123', date=2024-01-15, userName='user1', readings={gas=10.0, water=20.0}}");
    }

    @Test
    @DisplayName("Handle get all readings request with non-admin user")
    public void handleGetAllReadingsRequestWithNonAdminUser() {
        Mockito.when(userService.checkRole(user)).thenReturn(false);

        webService.handleGetAllReadingsRequest(user);

        Mockito.verify(userService).checkRole(user);
        Mockito.verify(meterService, Mockito.never()).getAllReadingsHistory(user);
        Assertions.assertThat(outContent.toString()).contains("Error: User does not have the necessary permissions to access all readings");
    }

    @Test
    @DisplayName("Handle get all readings request with null user")
    public void handleGetAllReadingsRequestWithNullUser() {
        User user = null;
        webService.handleGetAllReadingsRequest(user);
        Assertions.assertThat(outContent.toString()).contains("Error: User does not have the necessary permissions to access all readings");
    }
}