package com.ylab.app.service;

import com.ylab.app.model.*;

import java.util.*;

/**
 * MeterService interface for managing meter readings.
 *
 * @author razlivinsky
 * @since 28.01.2024
 */
public interface MeterService {
    /**
     * Gets the current readings for the specified user.
     *
     * @param user the user for whom the readings are retrieved
     * @return the list of current readings
     */
    List<MeterReading> getCurrentReadings(User user);

    /**
     * Submits a new reading for the specified user and meter.
     *
     * @param user        the user submitting the reading
     * @param numberMeter the meter number
     * @param type        the type of reading
     * @param value       the value of the reading
     */
    void submitReading(User user, String numberMeter, String type, double value);

    /**
     * Gets the readings for the specified user and month.
     *
     * @param user  the user for whom the readings are retrieved
     * @param month the month for which readings are retrieved
     * @return the list of readings for the specified month
     */
    List<MeterReading> getReadingsByMonth(User user, int month);

    /**
     * Gets the readings history for the specified user.
     *
     * @param user the user for whom the readings history is retrieved
     * @return the list of readings history for the specified user
     */
    List<MeterReading> getReadingsHistory(User user);

    /**
     * Gets the readings history for all users (for admin user).
     *
     * @param adminUser the admin user retrieving the readings history
     * @return the list of all readings history
     */
    List<MeterReading> getAllReadingsHistory(User adminUser);
}
