package com.ylab.app.dbService.dao.impl;

import com.ylab.app.dbService.dao.MeterReadingDao;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.MeterReadingDetails;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.ylab.app.constants.CreateSchemaSql.INSERT_METER_SCHEMA;
import static com.ylab.app.constants.CreateSchemaSql.INSERT_READING_DATA_SCHEMA;
import static com.ylab.app.constants.SqlQueryClass.*;

/**
 * Implementation of MeterReadingDao for interacting with the database to manage meter reading data.
 *
 * This class utilizes JdbcTemplate for database interaction and provides methods to insert meter readings and retrieve meter readings based on user or all readings.
 *
 * @author razlivinsky
 * @since 17.02.2024
 */
@Repository
public class MeterReadingDaoImpl implements MeterReadingDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Instantiates a new Meter reading dao.
     *
     * @param jdbcTemplate the jdbc template
     */
    public MeterReadingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MeterReading> meterReadingRowMapper = (rs, rowNum) -> {
        String numberMeter = rs.getString("number_meter");
        LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
        String username = rs.getString("user_name");
        User user = new User(username, "", UserRole.USER);
        MeterReading meterReading = new MeterReading(numberMeter, date, user);
        meterReading.setId(rs.getLong("id"));
        return meterReading;
    };

    private final RowMapper<MeterReadingDetails> meterReadingDetailsMapper = (rs, rowNum) -> {
        String type = rs.getString("type");
        double value = rs.getDouble("value");
        return new MeterReadingDetails(type, value);
    };

    /**
     * Inserts the provided meter reading and its details into the database.
     *
     * @param meterReading the meter reading object to be inserted
     * @throws DatabaseWriteException if an error occurs while interacting with the database
     */
    public void insertMeterReading(MeterReading meterReading) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_METER_SCHEMA, new String[]{"id"});
                ps.setString(1, meterReading.getNumberMeter());
                ps.setTimestamp(2, Timestamp.valueOf(meterReading.getDate()));
                ps.setString(3, meterReading.getUser().getUsername());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key == null) {
                throw new DatabaseWriteException("Failed to insert meter reading, no ID obtained.");
            }
            long newMeterReadingId = key.longValue();
            meterReading.setId(newMeterReadingId);
            List<MeterReadingDetails> detailsList = meterReading.getDetailsList();
            jdbcTemplate.batchUpdate(INSERT_READING_DATA_SCHEMA, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MeterReadingDetails details = detailsList.get(i);
                    ps.setLong(1, newMeterReadingId);
                    ps.setString(2, details.getType());
                    ps.setDouble(3, details.getValue());
                    details.setMeterReadingId(newMeterReadingId);
                }
                public int getBatchSize() {
                    return detailsList.size();
                }
            });
        } catch (DataAccessException e) {
            throw new DatabaseWriteException("Failed to insert meter reading " + e.getMessage());
        }
    }

    /**
     * Selects the list of current meter readings for the specified user from the database.
     *
     * @param user the user for whom to retrieve the meter readings
     * @return a list of the user's current meter readings
     * @throws DatabaseReadException if an error occurs while retrieving the data from the database
     */
    public List<MeterReading> selectCurrentMaterReading(User user) {
        try {
            List<MeterReading> meterReadings = jdbcTemplate.query(FOUND_MAX_ID, meterReadingRowMapper, user.getUsername());
            for (MeterReading meterReading : meterReadings) {
                List<MeterReadingDetails> detailsList = jdbcTemplate.query(All_READINGS, meterReadingDetailsMapper, meterReading.getId());
                for (MeterReadingDetails details : detailsList) {
                    meterReading.addReadingDetails(details.getType(), details.getValue());
                }
            }
            return meterReadings;
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve current meter readings for user " + user.getUsername() + e.getMessage());
        }
    }

    /**
     * Selects a list of meter readings for the specified user from the database.
     *
     * @param user the user for whom to retrieve the meter readings
     * @return a list of meter readings for the specified user
     * @throws DatabaseReadException if an error occurs while retrieving the data from the database
     */
    public List<MeterReading> selectByUserName(User user) {
        try {
            List<MeterReading> meterReadings = jdbcTemplate.query(SELECT_USER_NAME, meterReadingRowMapper, user.getUsername());
            for (MeterReading meterReading : meterReadings) {
                List<MeterReadingDetails> detailsList = jdbcTemplate.query(All_READINGS, meterReadingDetailsMapper, meterReading.getId());
                for (MeterReadingDetails details : detailsList) {
                    meterReading.addReadingDetails(details.getType(), details.getValue());
                }
            }
            return meterReadings;
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve current meter readings for user " + user.getUsername() + e.getMessage());
        }
    }

    /**
     * Selects all meter readings from the database.
     *
     * @return a list of all meter readings
     * @throws DatabaseReadException if an error occurs while retrieving the data from the database
     */
    public List<MeterReading> selectByAllMeterReadings() {
        try {
            List<MeterReading> meterReadings = jdbcTemplate.query(SELECT_ALL_METER_READINGS, meterReadingRowMapper);
            for (MeterReading meterReading : meterReadings) {
                List<MeterReadingDetails> detailsList = jdbcTemplate.query(All_READINGS, meterReadingDetailsMapper, meterReading.getId());
                for (MeterReadingDetails details : detailsList) {
                    meterReading.addReadingDetails(details.getType(), details.getValue());
                }
            }
            return meterReadings;
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve current meter readings for user " + e.getMessage());
        }
    }
}

