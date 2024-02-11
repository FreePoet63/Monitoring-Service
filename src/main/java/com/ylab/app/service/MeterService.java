package com.ylab.app.service;

import com.ylab.app.model.*;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.model.dto.UserDto;

import java.sql.SQLException;
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
    public List<MeterReadingDto> getCurrentReadings(User user);

    /**
     * Submits a new reading for the specified user and meter.
     *
     * @param user        the user submitting the reading
     * @param numberMeter the meter number
     * @param readings    list the type of reading
     */
    public MeterReadingDto submitReading(User user, String numberMeter, List<MeterReadingDetailsDto> readings);

    /**
     * Gets the readings for the specified user and month.
     *
     * @param user  the user for whom the readings are retrieved
     * @param month the month for which readings are retrieved
     * @return the list of readings for the specified month
     */
    public List<MeterReadingDto> getReadingsByMonth(User user, int month);

    /**
     * Gets the readings history for the specified user.
     *
     * @param user the user for whom the readings history is retrieved
     * @return the list of readings history for the specified user
     */
    public List<MeterReadingDto> getReadingsHistory(User user);

    /**
     * Gets the readings history for all users (for admin user).
     *
     * @param adminUser the admin user retrieving the readings history
     * @return the list of all readings history
     */
    public List<MeterReadingDto> getAllReadingsHistory(User adminUser);
}
