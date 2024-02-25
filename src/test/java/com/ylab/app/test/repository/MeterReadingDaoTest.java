package com.ylab.app.test.repository;

import com.ylab.app.dbService.dao.MeterReadingDao;
import com.ylab.app.dbService.dao.impl.MeterReadingDaoImpl;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.MeterReadingDetails;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.test.util.TestContainersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * MeterReadingDaoTest class
 *
 * @author HP
 * @since 04.02.2024
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
public class MeterReadingDaoTest extends TestContainersRepository {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MeterReadingDao meterReadingDao = new MeterReadingDaoImpl(jdbcTemplate);

    private User user1;
    private User user2;
    private MeterReading meterReading1;
    private MeterReading meterReading2;
    private List<MeterReading> meterReadingList;
    private MeterReadingDetails meterReadingDetails1;
    private MeterReadingDetails meterReadingDetails2;
    private List<MeterReadingDetails> meterReadingDetailsList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        user1 = new User("alice", "12345", UserRole.USER);
        user1.setId(1L);
        user2 = new User("bob", "67890", UserRole.ADMIN);
        user2.setId(2L);

        meterReadingDetails1 = new MeterReadingDetails("Electricity", 100.0);
        meterReadingDetails1.setMeterReadingId(1L);
        meterReadingDetails2 = new MeterReadingDetails("Gas", 200.0);
        meterReadingDetails2.setMeterReadingId(1L);
        meterReadingDetailsList = Arrays.asList(meterReadingDetails1, meterReadingDetails2);

        meterReading1 = new MeterReading("123456789", LocalDateTime.now(), user1);
        meterReading1.setId(1L);
        meterReading1.addReadingDetails("Electricity", 100.0);
        meterReading1.addReadingDetails("Gas", 200.0);
        meterReading2 = new MeterReading("987654321", LocalDateTime.now(), user2);
        meterReading2.setId(2L);
        meterReading2.addReadingDetails("Electricity", 100.0);
        meterReading2.addReadingDetails("Gas", 200.0);
        meterReadingList = Arrays.asList(meterReading1, meterReading2);
    }

    @Test
    @DisplayName("insertMeterReading inserts a new meter reading and its details into the database when successful")
    public void insertMeterReading_InsertNewMeterReading_WhenSuccessful() {
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenReturn(1);
        when(jdbcTemplate.batchUpdate(anyString(), Collections.singletonList(any()))).thenReturn(new int[]{1, 1});
        meterReadingDao.insertMeterReading(meterReading1);

        assertThat(meterReading1.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("insertMeterReading throws DatabaseWriteException when insertion fails")
    public void insertMeterReading_ThrowDatabaseWriteException_WhenInsertionFails() {
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenThrow(DataAccessException.class);

        assertThatThrownBy(() -> meterReadingDao.insertMeterReading(meterReading1))
                .isInstanceOf(DatabaseWriteException.class)
                .hasMessage("Failed to insert meter reading");
    }

    @Test
    @DisplayName("selectCurrentMaterReading returns a list of current meter readings for the specified user from the database when successful")
    public void selectCurrentMaterReading_ReturnListOfCurrentMeterReadings_WhenSuccessful() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString())).thenReturn(meterReadingList);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong())).thenReturn(meterReadingDetailsList);
        List<MeterReading> result = meterReadingDao.selectCurrentMaterReading(user1);

        assertThat(result).containsExactlyInAnyOrder(meterReading1, meterReading2);
    }

    @Test
    @DisplayName("selectCurrentMaterReading throws DatabaseReadException when retrieval fails")
    public void selectCurrentMaterReading_ThrowDatabaseReadException_WhenRetrievalFails() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString())).thenThrow(DataAccessException.class);

        assertThatThrownBy(() -> meterReadingDao.selectCurrentMaterReading(user1))
                .isInstanceOf(DatabaseReadException.class)
                .hasMessage("Failed to retrieve current meter readings for user " + user1.getUsername());
    }

    @Test
    @DisplayName("selectByUserName returns a list of meter readings for the specified user from the database when successful")
    public void selectByUserName_ReturnListOfMeterReadings_WhenSuccessful() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString())).thenReturn(meterReadingList);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong())).thenReturn(meterReadingDetailsList);
        List<MeterReading> result = meterReadingDao.selectByUserName(user1);

        assertThat(result).containsExactlyInAnyOrder(meterReading1, meterReading2);
    }

    @Test
    @DisplayName("selectByAllMeterReadings returns a list of all meter readings from the database when successful")
    public void selectByAllMeterReadings_ReturnListOfAllMeterReadings_WhenSuccessful() throws SQLException {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(meterReadingList);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong())).thenReturn(meterReadingDetailsList);
        List<MeterReading> result = meterReadingDao.selectByAllMeterReadings();

        assertThat(result).containsExactlyInAnyOrder(meterReading1, meterReading2);
    }

    @Test
    @DisplayName("selectByAllMeterReadings throws DatabaseReadException when retrieval fails")
    public void selectByAllMeterReadings_ThrowDatabaseReadException_WhenRetrievalFails() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenThrow(DataAccessException.class);

        assertThatThrownBy(() -> meterReadingDao.selectByAllMeterReadings())
                .isInstanceOf(DatabaseReadException.class)
                .hasMessage("Failed to retrieve all meter readings");
    }
}