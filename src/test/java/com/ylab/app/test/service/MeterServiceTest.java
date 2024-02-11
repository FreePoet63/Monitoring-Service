package com.ylab.app.test.service;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.impl.MeterReadingDaoImpl;
import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.exception.meterException.MeterReadingException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.*;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.model.dto.UserDto;
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
    private MeterReadingDto readingDto;

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
        List<MeterReadingDto> result = meterService.getCurrentReadings(expectedUser);
        assertThat(result).extracting(MeterReadingDto::getNumberMeter).contains(meterReading.getNumberMeter());
        assertThat(result).extracting(MeterReadingDto::getDetailsList).isNotNull();
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
            List<MeterReadingDetailsDto> details = List.of(new MeterReadingDetailsDto("gas", 10.0));
            String numberMeter = "8889";
            User user = new User("test", "test", UserRole.USER);

            doNothing().when(meterReadingDao).insertMeterReading(any(MeterReading.class));

            meterService.submitReading(user, numberMeter, details);
            verify(meterReadingDao).insertMeterReading(any(MeterReading.class));
    }

    @Test
    @DisplayName("Submit reading with null numberMeter")
    public void submitReadingWithNullNumberMeter() {
       String numberMeter = null;
        String type = "gas";
        double value = 10.0;

        List<MeterReadingDetailsDto> details = List.of(new MeterReadingDetailsDto(type, value));

        assertThatThrownBy(() -> meterService.submitReading(user, numberMeter, details))
                .isInstanceOf(MeterReadingException.class)
                .hasMessage("Invalid numberMeter");
    }

    @Test
    @DisplayName("Get readings by month for valid user and month")
    public void getReadingsByMonthForValidUserAndMonth() {
        int month = 1;
        User expectedUser = new User("test", "123", UserRole.USER);
        MeterReading meterReading = new MeterReading("123",
                LocalDateTime.of(2024, 1, 15, 12, 56), expectedUser);
        meterReading.addReadingDetails("gas", 10.0);
        meterReading.addReadingDetails("water", 20.0);
        MeterReading meterReading1 = new MeterReading("123",
                LocalDateTime.of(2024, 2, 15, 22, 0), expectedUser);
        meterReading1.addReadingDetails("gas", 15.0);
        meterReading1.addReadingDetails("water", 25.0);
        when(meterReadingDao.selectByNameUser(expectedUser)).thenReturn(List.of(meterReading));
        List<MeterReadingDto> result = meterService.getReadingsByMonth(expectedUser, month);

        assertThat(result).isNotNull();
        assertThat(result).extracting(MeterReadingDto::getNumberMeter).contains("123");
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
        User user1 = new User("test", "123", UserRole.USER);
        User user2 = new User("test", "456", UserRole.USER);
        MeterReading meterReading1 = new MeterReading("123",
                LocalDateTime.of(2024, 1, 15, 13, 0),user1);
        meterReading1.addReadingDetails("gas", 10.0);
        meterReading1.addReadingDetails("water", 20.0);

        MeterReading meterReading2 = new MeterReading("456",
                LocalDateTime.of(2024, 2, 15, 17, 30), user2);
        meterReading2.addReadingDetails("gas", 15.0);
        meterReading2.addReadingDetails("water", 25.0);

        when(meterReadingDao.selectByAllMeterReadings()).thenReturn(List.of(meterReading1, meterReading2));

        List<MeterReadingDto> result = meterService.getAllReadingsHistory(adminUser);

        assertThat(result).isNotNull()
                .hasSize(2)
                .extracting(
                        MeterReadingDto::getNumberMeter,
                        MeterReadingDto::getUser)
                .containsExactly(
                        tuple("123", user1),
                        tuple("456", user2)
                );
    }

    @Test
    @DisplayName("Get all readings history for admin user")
    public void testGetAllReadingsHistoryForAdminUser() throws SQLException {
        User adminUser = new User("adminUser", "admin", UserRole.ADMIN);
        when(userService.hasRoleAdmin(adminUser)).thenReturn(true);

        User user1 = new User("user1", "user1", UserRole.USER);
        User user2 = new User("user2", "user2", UserRole.USER);
        MeterReading meterReading = new MeterReading("123",
                LocalDateTime.of(2024, 1, 15, 12, 56), user1);
        meterReading.addReadingDetails("gas", 10.0);
        meterReading.addReadingDetails("water", 20.0);
        MeterReading meterReading1 = new MeterReading("123",
                LocalDateTime.of(2024, 2, 15, 22, 0), user2);
        meterReading1.addReadingDetails("gas", 15.0);
        meterReading1.addReadingDetails("water", 25.0);
        when(meterReadingDao.selectByAllMeterReadings()).thenReturn(List.of(meterReading));

        List<MeterReadingDto> result = meterService.getAllReadingsHistory(adminUser);

        assertThat(result).isNotNull();
    }
}