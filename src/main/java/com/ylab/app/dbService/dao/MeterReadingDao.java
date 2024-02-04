package com.ylab.app.dbService.dao;

import com.ylab.app.model.MeterReading;
import com.ylab.app.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * The MeterReadingDao interface provides methods for data access related to meter readings in the database.
 * It includes methods for inserting meter readings, selecting current meter readings for a user,
 * selecting all meter readings for a user, finding the total sum of readings for a specific type and user,
 * and finding a meter reading by its ID.
 * @author razlivinsky
 * @since 03.02.2024
 */
public interface MeterReadingDao {
    /**
     * Inserts the provided meter reading into the database.
     *
     * @param meterReading the meter reading object to be inserted
     * @throws SQLException if an error occurs while interacting with the database
     */
    public void insertMeterReading(MeterReading meterReading) throws SQLException;

    /**
     * Selects the list of current meter readings for the specified user from the database.
     *
     * @param user the user for whom to retrieve the current meter readings
     * @return a list of current meter readings for the specified user
     */
    public List<MeterReading> selectCurrentMaterReading(User user);

    /**
     * Selects a list of meter readings for the specified user from the database.
     *
     * @param user the user for whom to retrieve the meter readings
     * @return list of meter readings for the specified user
     */
    public List<MeterReading> selectByNameUser(User user);

    /**
     * Selects all meter readings from the database.
     *
     * @return list of all meter readings
     * @throws SQLException if an error occurs while interacting with the database
     */
    public List<MeterReading> selectByAllMeterReadings() throws SQLException;

    /**
     * Finds the total sum of readings for a specific type and user.
     *
     * @param type type for which to find the sum of readings
     * @param user the user for whom to find the sum of readings
     * @return the total sum of readings for the specified type and user
     */
    public double findToSumReadingForType(String type, User user);

    /**
     * Finds a meter reading by its ID.
     *
     * @param id the ID of the meter reading to find
     * @return meter reading with the specified ID
     * @throws SQLException if an error occurs while interacting with the database
     */
    public MeterReading findById(long id) throws SQLException;
}
