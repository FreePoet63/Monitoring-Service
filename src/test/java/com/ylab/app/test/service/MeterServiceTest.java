package com.ylab.app.test.service;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.impl.MeterReadingDaoImpl;
import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.exception.meterException.MeterReadingException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.*;
import com.ylab.app.service.MeterService;
import com.ylab.app.service.impl.MeterServiceImpl;
import com.ylab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

/**
 * MeterServiceTest class
 *
 * @author razlivinsky
 * @since 27.01.2024
 */
@ExtendWith(MockitoExtension.class)
public class MeterServiceTest {
    @InjectMocks
    private MeterServiceImpl meterService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserDaoImpl dao;

    @Mock
    private MeterReadingDaoImpl meterReadingDao;

    @Mock
    private Session session;

    @Mock
    private User user;

    @Test
    @DisplayName("Get current readings for valid user")
    public void getCurrentReadingsForValidUser() throws SQLException {
        User expectedUser = new User("test", "123", UserRole.USER);
        MeterReading meterReading = new MeterReading("123", LocalDateTime.now(), expectedUser);
        meterReading.addReadingDetails("gas", 10.0);
        when(meterReadingDao.selectCurrentMaterReading(expectedUser)).thenReturn(List.of(meterReading));
        List<MeterReading> result = meterService.getCurrentReadings(expectedUser);
        assertThat(result).isSameAs(List.of(meterReading));
    }

    @Test
    @DisplayName("Get current readings for null user")
    public void getCurrentReadingsForNullUser() {
        User user = null;

        assertThatThrownBy(() -> meterService.getCurrentReadings(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid user");
    }

    @Test
    @DisplayName("Submit reading with valid data")
    public void submitReadingWithValidData() throws SQLException {
            List<MeterReadingDetails> details = List.of(new MeterReadingDetails(1L, "gas", 10.0));
            String numberMeter = "8889";
            String name = user.getName();
            String password = user.getPassword();
            UserRole role = user.getRole();
            doNothing().when(userService).registerUser(name, password, role);
            when(dao.findUserByNameAndPassword(name, password)).thenReturn(user);
            when(userService.loginUser(name, password)).thenReturn(user);
            when(session.getUser()).thenReturn(user);
            doNothing().when(meterReadingDao).insertMeterReading(any(MeterReading.class));

            meterService.submitReading(user, numberMeter, details);
            verify(dao).insertUser(user);
            verify(meterReadingDao).insertMeterReading(any(MeterReading.class));
    }

    @Test
    @DisplayName("Submit reading with null numberMeter")
    public void submitReadingWithNullNumberMeter() {
       String numberMeter = null;
        String type = "gas";
        double value = 10.0;

        List<MeterReadingDetails> details = List.of(new MeterReadingDetails(1L, type, value));

        assertThatThrownBy(() -> meterService.submitReading(user, numberMeter, details))
                .isInstanceOf(MeterReadingException.class)
                .hasMessage("Invalid numberMeter");
    }

    @Test
    @DisplayName("Get readings by month for valid user and month")
    public void getReadingsByMonthForValidUserAndMonth() {
        int month = 1;
        MeterReading meterReading = new MeterReading("123",
                LocalDateTime.of(2024, 1, 15, 12, 56), new User("test", "123", UserRole.USER));
        meterReading.addReadingDetails("gas", 10.0);
        meterReading.addReadingDetails("water", 20.0);
        MeterReading meterReading1 = new MeterReading("123",
                LocalDateTime.of(2024, 2, 15, 22, 0), new User("test", "123", UserRole.USER));
        meterReading1.addReadingDetails("gas", 15.0);
        meterReading1.addReadingDetails("water", 25.0);

        List<MeterReading> result = meterService.getReadingsByMonth(user, month);

        assertThat(result).isNotNull()
                .hasSize(1)
                .first()
                .extracting(MeterReading::getNumberMeter, MeterReading::getDate, MeterReading::getUser, MeterReading::getDetailsList)
                .containsExactly("123", LocalDateTime.of(2024, 1, 15, 12, 56), "user1",
                        List.of("gas", 10.0, "water", 20.0));
   }

    @Test
    @DisplayName("Get readings by month for null user")
    public void getReadingsByMonthForNullUser() {
        User user = null;
        int month = 1;

        assertThatThrownBy(() -> meterService.getReadingsByMonth(user, month))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid user");
    }

    @Test
    @DisplayName("Get all readings history for valid and authorized user")
    public void getAllReadingsHistoryForValidAndAuthorizedUser() throws SQLException {
        User adminUser = new User("admin", "admin", UserRole.ADMIN);
        when(userService.hasRoleAdmin(adminUser)).thenReturn(true);

        MeterReading meterReading1 = new MeterReading("123",
                LocalDateTime.of(2024, 1, 15, 13, 0), new User("test", "123", UserRole.USER));
        meterReading1.addReadingDetails("gas", 10.0);
        meterReading1.addReadingDetails("water", 20.0);

        MeterReading meterReading2 = new MeterReading("456",
                LocalDateTime.of(2024, 2, 15, 17, 30), new User("test", "123", UserRole.USER));
        meterReading2.addReadingDetails("gas", 15.0);
        meterReading2.addReadingDetails("water", 25.0);

        when(meterReadingDao.selectByAllMeterReadings()).thenReturn(List.of(meterReading1, meterReading2));

        List<MeterReading> result = meterService.getAllReadingsHistory(adminUser);

        assertThat(result).isNotNull()
                .hasSize(2)
                .extracting(
                        MeterReading::getNumberMeter,
                        MeterReading::getDate,
                        MeterReading::getUser,
                        meterReading -> meterReading.getDetailsList().stream().flatMap(details -> Stream.of(details.getType(), details.getValue())).collect(Collectors.toList())
                )
                .containsExactly(
                        tuple("123", LocalDateTime.of(2024, 1, 15, 13, 0), "user1", List.of("gas", 10.0, "water", 20.0)),
                        tuple("456", LocalDateTime.of(2024, 2, 15, 17, 30), "user2", List.of("gas", 15.0, "water", 25.0))
                );
    }

    @Test
    @DisplayName("Get all readings history for admin user")
    public void testGetAllReadingsHistoryForAdminUser() throws SQLException {
        User adminUser = new User("adminUser", "admin", UserRole.ADMIN);
        when(userService.hasRoleAdmin(adminUser)).thenReturn(true);

        User user1 = new User("user1", "user1", UserRole.USER);
        User user2 = new User("user2", "user2", UserRole.USER);
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        List<MeterReading> result = meterService.getAllReadingsHistory(adminUser);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(MeterReading::getUser)
                .containsExactly(user1, user2);
    }
}