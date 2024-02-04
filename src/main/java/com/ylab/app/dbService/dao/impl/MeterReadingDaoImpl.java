package com.ylab.app.dbService.dao.impl;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.MeterReadingDao;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.MeterReadingDetails;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.ylab.app.constants.SqlQueryClass.*;

/**
 * The MeterReadingDaoImpl class provides methods for data access related to meter readings in the database.
 * It includes methods for inserting meter readings, selecting meter readings, finding total sum of readings for a specific type, and finding a reading by its ID.
 *
 * @author razlivinsky
 * @since 29.01.2024
 */
public class MeterReadingDaoImpl implements MeterReadingDao {
    /**
     * Inserts the provided meter reading and its details into the database.
     *
     * @param meterReading the meter reading object to be inserted
     * @throws DatabaseWriteException if an error occurs while interacting with the database
     */
    public void insertMeterReading(MeterReading meterReading) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmtMtr = conn.prepareStatement(insertMtrSQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmtMtr.setString(1, meterReading.getNumberMeter());
            pstmtMtr.setTimestamp(2, Timestamp.valueOf(meterReading.getDate()));
            pstmtMtr.setString(3, meterReading.getUser().getName());
            pstmtMtr.executeUpdate();

            try (ResultSet rs = pstmtMtr.getGeneratedKeys()) {
                if (rs.next()) {
                    long mtrId = rs.getLong(1);
                    for (MeterReadingDetails details : meterReading.getDetailsList()) {
                        details.setMeterReadingId(mtrId);
                    }
                } else {
                    throw new DatabaseWriteException("Inserting meter reading failed, no ID obtained.");
                }
            }
            try (PreparedStatement pstmtReading = conn.prepareStatement(insertReadingSQL)) {
                for (MeterReadingDetails details : meterReading.getDetailsList()) {
                    pstmtReading.setLong(1, details.getMeterReadingId());
                    pstmtReading.setString(2, details.getType());
                    pstmtReading.setDouble(3, details.getValue());
                    pstmtReading.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseWriteException("invalid " + e.getMessage());
        }
    }

    /**
     * Selects the list of current meter readings for the specified user from the database.
     *
     * @param user the user for whom to retrieve the meter readings
     * @return a list of the user's current meter readings
     */
    public List<MeterReading> selectCurrentMaterReading(User user) {
        List<MeterReading> meterReadings = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            try (PreparedStatement pstmtMaxId = conn.prepareStatement(selectMaxIdSQL)) {
                pstmtMaxId.setString(1, user.getName());
                try (ResultSet rsMaxId = pstmtMaxId.executeQuery()) {
                    if (rsMaxId.next()) {
                        long maxId = rsMaxId.getLong(1);
                        try (PreparedStatement pstmtAll = conn.prepareStatement(selectAllSQL)) {
                            pstmtAll.setLong(1, maxId);
                            try (ResultSet rsAll = pstmtAll.executeQuery()) {
                                while (rsAll.next()) {
                                    MeterReading meterReading = new MeterReading(
                                            rsAll.getString("number_meter"),
                                            rsAll.getTimestamp("date").toLocalDateTime(),
                                            user);
                                    meterReading.setUser(user);
                                    String type = rsAll.getString("type");
                                    double value = rsAll.getDouble("value");
                                    meterReading.addReadingDetails(type, value);
                                    meterReadings.add(meterReading);
                                }
                            }
                        }
                    } else {
                        return meterReadings;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return meterReadings;
    }

    /**
     * Selects a list of meter readings for the specified user from the database.
     *
     * @param user the user for whom to retrieve the meter readings
     * @return a list of meter readings for the specified user
     */
    public List<MeterReading> selectByNameUser(User user) {
        List<MeterReading> meterReadings = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectUserNameSQL)) {
            pstmt.setString(1, user.getName());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MeterReading meterReading = new MeterReading(
                            rs.getString("number_meter"),
                            rs.getTimestamp("date").toLocalDateTime(),
                            user);
                    meterReading.setUser(user);
                    String type = rs.getString("type");
                    double value = rs.getDouble("value");
                    meterReading.addReadingDetails(type, value);
                    meterReadings.add(meterReading);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return meterReadings;
    }

    /**
     * Selects all meter readings from the database.
     *
     * @return list of all meter readings
     * @throws DatabaseReadException if an error occurs while interacting with the database
     */
    public List<MeterReading> selectByAllMeterReadings() throws SQLException {
        List<MeterReading> meterReadings = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllMeterReadingSQL)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("user_name");
                    User user = new User(username, "", UserRole.USER);
                    MeterReading meterReading = new MeterReading(
                            rs.getString("number_meter"),
                            rs.getTimestamp("date").toLocalDateTime(), user);
                    String type = rs.getString("type");
                    double value = rs.getDouble("value");
                    meterReading.addReadingDetails(type, value);
                    meterReadings.add(meterReading);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return meterReadings;
    }


    /**
     * Finds the total sum of readings for a specific type and user.
     *
     * @param type the type for which to find the sum of readings
     * @param user the user for whom to find the sum of readings
     * @return the total sum of readings for the specified type and user
     */
    public double findToSumReadingForType(String type, User user) {
        double sum = 0;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSumValueSQL)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, type);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double value = rs.getDouble("value");
                    sum += value;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return sum;
    }

    /**
     * Finds a meter reading by its ID.
     *
     * @param id the ID of the meter reading to find
     * @return the meter reading with the specified ID
     * @throws DatabaseReadException if an error occurs while interacting with the database
     */
    public MeterReading findById(long id) throws SQLException {
        MeterReading meterReading = null;
        try (Connection conn = ConnectionManager.getConnection()) {
            try (PreparedStatement pstmtMtr = conn.prepareStatement(selectMtrSQL)) {
                pstmtMtr.setLong(1, id);
                try (ResultSet rsMtr = pstmtMtr.executeQuery()) {
                    if (rsMtr.next()) {
                        String username = rsMtr.getString("user_name");
                        User user = new User(username, "", UserRole.USER);
                        meterReading = new MeterReading(
                                rsMtr.getString("number_meter"),
                                rsMtr.getTimestamp("date").toLocalDateTime(), user);
                    } else {
                        return null;
                    }
                }
            }
            try (PreparedStatement pstmtReading = conn.prepareStatement(selectReadingSQL)) {
                pstmtReading.setLong(1, id);
                try (ResultSet rsReading = pstmtReading.executeQuery()) {
                    while (rsReading.next()) {
                        String type = rsReading.getString("type");
                        double value = rsReading.getDouble("value");
                        meterReading.addReadingDetails(type, value);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return meterReading;
    }
}

