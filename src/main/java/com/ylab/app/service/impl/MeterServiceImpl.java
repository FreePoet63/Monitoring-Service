package com.ylab.app.service.impl;

import com.ylab.app.model.*;
import com.ylab.app.service.*;

import java.time.*;
import java.util.*;

/**
 * MeterServiceImpl class for managing meter readings.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class MeterServiceImpl implements MeterService {
    private UserServiceImpl userService;
    private List<MeterReading> readings;

    /**
     * Instantiates a new Meter service.
     *
     * @param userService the user service to be used
     */
    public MeterServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
        this.readings = new ArrayList<>();
    }

    /**
     * Gets the current readings for the specified user.
     *
     * @param user the user for whom the current readings are retrieved
     * @return the list of current readings
     * @throws IllegalArgumentException if the user is invalid
     */
    public List<MeterReading> getCurrentReadings(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        List<MeterReading> userReadings = user.getReadings();
        List<MeterReading> currentReadings = new ArrayList<>();
        LocalTime now = LocalTime.now();
        MeterReading lastReading = null;
        for (MeterReading reading : userReadings) {
            LocalTime readingTime = reading.getDate().toLocalTime();
            if (readingTime.equals(now) || readingTime.isBefore(now)) {
                if (lastReading == null || readingTime.isAfter(lastReading.getDate().toLocalTime())) {
                    lastReading = reading;
                }
            }
        }
        if (lastReading != null) {
            currentReadings.add(lastReading);
        }
        return currentReadings;
    }

    /**
     * Submits a new reading for the specified user and meter.
     *
     * @param user        the user submitting the reading
     * @param numberMeter the meter number for the reading
     * @param type        the type of reading
     * @param value       the value of the reading
     * @throws IllegalArgumentException if the meter number, type, or value is invalid
     */
    public void submitReading(User user, String numberMeter, String type, double value) {
        if (numberMeter == null || numberMeter.isEmpty() || numberMeter.contains(" ")) {
            throw new IllegalArgumentException("Invalid numberMeter");
        }
        if (type == null || type.isEmpty() || type.contains(" ")) {
            throw new IllegalArgumentException("Invalid type");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Invalid value");
        }
        MeterReading reading = null;
        for (MeterReading existingReading : readings) {
            boolean sameMonth = existingReading.getDate().getMonth() == LocalDateTime.now().getMonth();
            boolean sameYear = existingReading.getDate().getYear() == LocalDateTime.now().getYear();
            if (existingReading.getNumberMeter().equals(numberMeter) && sameMonth && sameYear) {
                reading = existingReading;
                break;
            }
        }
        if (reading == null) {
            reading = new MeterReading(numberMeter, LocalDateTime.now(), user.getName());
            readings.add(reading);
            user.addReading(reading);
        }
        reading.addReading(type, value);
    }

    /**
     * Gets the readings for the specified user and month.
     *
     * @param user  the user for whom the readings are retrieved
     * @param month the month for which readings are retrieved
     * @return the list of readings for the specified month
     * @throws IllegalArgumentException if the user is invalid or the month is out of range
     */
    public List<MeterReading> getReadingsByMonth(User user, int month) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month");
        }
        List<MeterReading> userReadings = user.getReadings();
        List<MeterReading> monthlyReadings = new ArrayList<>();
        for (MeterReading reading : userReadings) {
            if (reading.getDate().getMonthValue() == month) {
                monthlyReadings.add(reading);
            }
        }
        return monthlyReadings;
    }

    /**
     * Gets the readings history for the specified user.
     *
     * @param user the user for whom the readings history is retrieved
     * @return the list of readings history for the specified user
     * @throws IllegalArgumentException if the user is invalid
     */
    public List<MeterReading> getReadingsHistory(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        return user.getReadings();
    }

    /**
     * Gets the readings history for all users (for admin user).
     *
     * @param adminUser the admin user retrieving the readings history
     * @return the list of all readings history
     * @throws IllegalArgumentException if the admin user is invalid or unauthorized
     */
    public List<MeterReading> getAllReadingsHistory(User adminUser) {
        if (adminUser == null || !userService.checkRole(adminUser)) {
            throw new IllegalArgumentException("Invalid or unauthorized user");
        }
        List<MeterReading> allReadings = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            allReadings.addAll(user.getReadings());
        }
        return allReadings;
    }
}
