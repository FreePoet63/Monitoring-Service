package com.ylab.app.test.service;

import com.ylab.app.model.*;
import com.ylab.app.service.*;
import com.ylab.app.service.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * MeterServiceTest class
 *
 * @author HP
 * @since 27.01.2024
 */
@ExtendWith(MockitoExtension.class)
public class MeterServiceTest {
    private MeterService meterService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        meterService = new MeterServiceImpl(userService);
    }

    @Test
    @DisplayName("Get current readings for valid user")
    public void getCurrentReadingsForValidUser() {
        MeterReading meterReading = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 14, 45), "user1");
        meterReading.addReading("gas", 10.0);
        meterReading.addReading("water", 20.0);
        MeterReading meterReading1 = new MeterReading("123", LocalDateTime.of(2024, 2, 15, 13, 0), "user1");
        meterReading1.addReading("gas", 15.0);
        meterReading1.addReading("water", 25.0);

        when(user.getReadings()).thenReturn(List.of(meterReading, meterReading1));

        List<MeterReading> result = meterService.getCurrentReadings(user);

        assertThat(result).isNotNull()
                .hasSize(1)
                .first()
                .extracting(MeterReading::getNumberMeter, MeterReading::getDate, MeterReading::getUser, MeterReading::getMap)
                .containsExactly("123", LocalDateTime.of(2024, 1, 15, 14, 45), "user1",
                        Map.of( "gas", List.of(10.0), "water", List.of(20.0)));
    }

    @Test
    @DisplayName("Get current readings for null user")
    public void getCurrentReadingsForNullUser() {
        User user = null;

        assertThatThrownBy(() -> meterService.getCurrentReadings(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid user");
    }

    @Test
    @DisplayName("Submit reading with valid data")
    public void submitReadingWithValidData() {
        String numberMeter = "123";
        String type = "gas";
        double value = 10.0;

        User user = new User("user1", "111", "user");

        meterService.submitReading(user, numberMeter, type, value);

        List<MeterReading> currentReadings = meterService.getCurrentReadings(user);
        assertThat(currentReadings)
                .isNotNull()
                .hasSize(1)
                .first()
                .satisfies(reading -> {
                    assertThat(reading.getNumberMeter()).isEqualTo(numberMeter);
                    assertThat(reading.getUser()).isEqualTo("user1");
                    assertThat(reading.getMap()).containsEntry(type, List.of(value));
                });
    }

    @Test
    @DisplayName("Submit reading with null numberMeter")
    public void submitReadingWithNullNumberMeter() {
        String numberMeter = null;
        String type = "gas";
        double value = 10.0;

        assertThatThrownBy(() -> meterService.submitReading(user, numberMeter, type, value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid numberMeter");
    }

    @Test
    @DisplayName("Get readings by month for valid user and month")
    public void getReadingsByMonthForValidUserAndMonth() {
        int month = 1;
        MeterReading meterReading = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 12, 56), "user1");
        meterReading.addReading("gas", 10.0);
        meterReading.addReading("water", 20.0);
        MeterReading meterReading1 = new MeterReading("123", LocalDateTime.of(2024, 2, 15, 22, 0), "user1");
        meterReading1.addReading("gas", 15.0);
        meterReading1.addReading("water", 25.0);

        when(user.getReadings()).thenReturn(List.of(meterReading, meterReading1));

        List<MeterReading> result = meterService.getReadingsByMonth(user, month);

        assertThat(result).isNotNull()
                .hasSize(1)
                .first()
                .extracting(MeterReading::getNumberMeter, MeterReading::getDate, MeterReading::getUser, MeterReading::getMap)
                .containsExactly("123", LocalDateTime.of(2024, 1, 15, 12, 56), "user1", Map.of("gas", List.of(10.0), "water", List.of(20.0)));
    }

    @Test
    @DisplayName("Get readings by month for null user")
    public void getReadingsByMonthForNullUser() {
        User user = null;
        int month = 1;

        assertThatThrownBy(() -> meterService.getReadingsByMonth(user, month))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid user");
    }

    @Test
    @DisplayName("Get readings history for valid user")
    public void getReadingsHistoryForValidUser() {
        MeterReading meterReading = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 13, 0), "user1");
        meterReading.addReading("gas", 10.0);
        meterReading.addReading("water", 20.0);
        MeterReading meterReading1 = new MeterReading("123", LocalDateTime.of(2024, 2, 15, 17, 30), "user1");
        meterReading1.addReading("gas", 15.0);
        meterReading1.addReading("water", 25.0);

        when(user.getReadings()).thenReturn(List.of(meterReading, meterReading1));

        List<MeterReading> result = meterService.getReadingsHistory(user);

        assertThat(result).isNotNull()
                .hasSize(2)
                .extracting(MeterReading::getNumberMeter, MeterReading::getDate, MeterReading::getUser, MeterReading::getMap)
                .containsExactly(
                        tuple("123", LocalDateTime.of(2024, 1, 15, 13, 0), "user1", Map.of("gas", List.of(10.0), "water", List.of(20.0))),
                        tuple("123", LocalDateTime.of(2024, 2, 15, 17, 30), "user1", Map.of("gas", List.of(15.0), "water", List.of(25.0)))
                );
    }

    @Test
    @DisplayName("Get all readings history for admin user")
    public void testGetAllReadingsHistoryForAdminUser() {
        User adminUser = new User("adminUser", "admin", "admin");
        when(userService.checkRole(adminUser)).thenReturn(true);

        User user1 = new User("user1", "user1", "user");
        User user2 = new User("user2", "user2", "user");
        user1.addReading(new MeterReading("123", LocalDateTime.now(), "user1"));
        user2.addReading(new MeterReading("456", LocalDateTime.now(), "user2"));
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        List<MeterReading> result = meterService.getAllReadingsHistory(adminUser);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(MeterReading::getUser)
                .containsExactly("user1", "user2");
    }
}