package com.ylab.app.test.repository;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.MeterReadingDao;
import com.ylab.app.dbService.dao.impl.MeterReadingDaoImpl;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.test.util.TestContainersRepository;
import com.ylab.app.test.util.TestDatabaseConnection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.ylab.app.test.util.TestDataReader.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MeterReadingDaoTest class
 *
 * @author HP
 * @since 04.02.2024
 */
@Testcontainers
public class MeterReadingDaoTest extends TestContainersRepository {
    private MeterReadingDao meterReadingDao = new MeterReadingDaoImpl();

    @BeforeEach
    public void setUp() throws SQLException {
        TestDatabaseConnection manager = new TestDatabaseConnection();
        manager.setConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
    }

    @Test
    @DisplayName("Insert meter reading into database")
    public void testInsertMeterReading() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        MeterReading meterReading = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 13, 0), user);
        meterReading.addReadingDetails("gas", 10.0);
        meterReading.addReadingDetails("water", 20.0);

        meterReadingDao.insertMeterReading(meterReading);

        MeterReading retrievedMeterReading = meterReadingDao.findById(meterReading.getId());
        assertEquals(meterReading.getNumberMeter(), retrievedMeterReading.getNumberMeter());
        assertEquals(meterReading.getDate(), retrievedMeterReading.getDate());
        assertEquals(meterReading.getUser(), retrievedMeterReading.getUser());
        assertEquals(meterReading.getDetailsList(), retrievedMeterReading.getDetailsList());
    }

    @Test
    @DisplayName("Select current meter readings for user")
    public void testSelectCurrentMeterReadingForUser() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        MeterReading meterReading1 = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 13, 0), user);
        meterReading1.addReadingDetails("gas", 10.0);
        meterReading1.addReadingDetails("water", 20.0);
        MeterReading meterReading2 = new MeterReading("456", LocalDateTime.of(2024, 2, 15, 17, 30), user);
        meterReading2.addReadingDetails("gas", 15.0);
        meterReading2.addReadingDetails("water", 25.0);

        meterReadingDao.insertMeterReading(meterReading1);
        meterReadingDao.insertMeterReading(meterReading2);

        List<MeterReading> result = meterReadingDao.selectCurrentMaterReading(user);

        assertThat(result).isNotNull()
                .extracting(
                        MeterReading::getNumberMeter,
                        MeterReading::getDate,
                        MeterReading::getUser,
                        MeterReading::getDetailsList
                )
                .containsAnyOf(
                        tuple("123", LocalDateTime.of(2024, 1, 15, 13, 0), user, List.of("gas", 10.0, "water", 20.0)),
                        tuple("456", LocalDateTime.of(2024, 2, 15, 17, 30), user, List.of("gas", 15.0, "water", 25.0))
                );
    }

    @Test
    @DisplayName("Select meter readings by user name")
    public void testSelectMeterReadingsByUserName() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        MeterReading meterReading1 = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 13, 0), user);
        meterReading1.addReadingDetails("gas", 10.0);
        meterReading1.addReadingDetails("water", 20.0);
        MeterReading meterReading2 = new MeterReading("456", LocalDateTime.of(2024, 2, 15, 17, 30), user);
        meterReading2.addReadingDetails("gas", 15.0);
        meterReading2.addReadingDetails("water", 25.0);

        meterReadingDao.insertMeterReading(meterReading1);
        meterReadingDao.insertMeterReading(meterReading2);

        List<MeterReading> result = meterReadingDao.selectByNameUser(user);

        assertThat(result).isNotNull()
                .hasSize(2)
                .extracting(
                        MeterReading::getNumberMeter,
                        MeterReading::getDate,
                        MeterReading::getUser,
                        MeterReading::getDetailsList
                )
                .containsExactlyInAnyOrder(
                        tuple("123", LocalDateTime.of(2024, 1, 15, 13, 0), user, List.of("gas", 10.0, "water", 20.0)),
                        tuple("456", LocalDateTime.of(2024, 2, 15, 17, 30), user, List.of("gas", 15.0, "water", 25.0))
                );
    }

    @Test
    @DisplayName("Select all meter readings")
    public void testSelectAllMeterReadings() throws SQLException {
        User user1 = new User("Alice", "1234", UserRole.USER);
        User user2 = new User("Bob", "4321", UserRole.ADMIN);
        MeterReading meterReading1 = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 13, 0), user1);
        meterReading1.addReadingDetails("gas", 10.0);
        meterReading1.addReadingDetails("water", 20.0);
        MeterReading meterReading2 = new MeterReading("456", LocalDateTime.of(2024, 2, 15, 17, 30), user2);
        meterReading2.addReadingDetails("gas", 15.0);
        meterReading2.addReadingDetails("water", 25.0);

        meterReadingDao.insertMeterReading(meterReading1);
        meterReadingDao.insertMeterReading(meterReading2);

        List<MeterReading> result = meterReadingDao.selectByAllMeterReadings();

        assertThat(result).isNotNull()
                .hasSize(2)
                .extracting(
                        MeterReading::getNumberMeter,
                        MeterReading::getDate,
                        MeterReading::getUser,
                        MeterReading::getDetailsList
                )
                .containsExactlyInAnyOrder(
                        tuple("123", LocalDateTime.of(2024, 1, 15, 13, 0), user1, List.of("gas", 10.0, "water", 20.0)),
                        tuple("456", LocalDateTime.of(2024, 2, 15, 17, 30), user2, List.of("gas", 15.0, "water", 25.0))
                );
    }

    @Test
    @DisplayName("Find sum of readings for type and user")
    public void testFindSumOfReadingsForTypeAndUser() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        MeterReading meterReading1 = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 13, 0), user);
        meterReading1.addReadingDetails("gas", 10.0);
        meterReading1.addReadingDetails("water", 20.0);
        MeterReading meterReading2 = new MeterReading("456", LocalDateTime.of(2024, 2, 15, 17, 30), user);
        meterReading2.addReadingDetails("gas", 15.0);
        meterReading2.addReadingDetails("water", 25.0);

        meterReadingDao.insertMeterReading(meterReading1);
        meterReadingDao.insertMeterReading(meterReading2);

        double result = meterReadingDao.findToSumReadingForType("gas", user);

        assertEquals(25.0, result);
    }

    @Test
    @DisplayName("Find meter reading by id")
    public void testFindMeterReadingById() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        MeterReading meterReading = new MeterReading("123", LocalDateTime.of(2024, 1, 15, 13, 0), user);
        meterReading.addReadingDetails("gas", 10.0);
        meterReading.addReadingDetails("water", 20.0);

        meterReadingDao.insertMeterReading(meterReading);

        MeterReading result = meterReadingDao.findById(meterReading.getId());

        assertEquals(meterReading.getNumberMeter(), result.getNumberMeter());
        assertEquals(meterReading.getDate(), result.getDate());
        assertEquals(meterReading.getUser(), result.getUser());
        assertEquals(meterReading.getDetailsList(), result.getDetailsList());
    }
}