package com.ylab.app.service.impl;

import com.ylab.app.model.*;
import com.ylab.app.service.*;

import java.util.*;

/**
 * WebServiceImpl class for handling user requests.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class WebServiceImpl implements WebService {
    private UserServiceImpl userService;
    private MeterServiceImpl meterService;

    /**
     * Instantiates a new Web service.
     *
     * @param userService  the user service to be used
     * @param meterService the meter service to be used
     */
    public WebServiceImpl(UserServiceImpl userService, MeterServiceImpl meterService) {
        this.userService = userService;
        this.meterService = meterService;
    }

    /**
     * Handles registration request for a new user.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @param role     the role of the user
     */
    public void handleRegisterRequest(String name, String password, String role) {
        try {
            userService.registerUser(name, password, role);
            System.out.println("User registered successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles login request for a user.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @return the user object if login is successful, null otherwise
     */
    public User handleLoginRequest(String name, String password) {
        try {
            User user = userService.loginUser(name, password);
            System.out.println("User logged in successfully");
            return user;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Handles request to fetch current readings for a user.
     *
     * @param user the user for current readings are requested
     */
    public void handleGetCurrentReadingsRequest(User user) {
        try {
            List<MeterReading> currentReadings = meterService.getCurrentReadings(user);
            System.out.println(user.getName() + " " + currentReadings.size() + " current readings");
            for (MeterReading reading : currentReadings) {
                System.out.println(reading);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles request to submit a new reading for a user's meter.
     *
     * @param user        the user submitting the reading
     * @param numberMeter the meter number for which the reading is being submitted
     * @param type        the type of the reading
     * @param value       the value of the reading
     */
    public void handleSubmitReadingRequest(User user, String numberMeter, String type, double value) {
        try {
            meterService.submitReading(user, numberMeter, type, value);
            System.out.println("Reading submitted successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles request to fetch readings for a user and a specified month.
     *
     * @param user  the user for whom the readings are requested
     * @param month the month for which the readings are requested
     */
    public void handleGetReadingsByMonthRequest(User user, int month) {
        try {
            List<MeterReading> monthlyReadings = meterService.getReadingsByMonth(user, month);
            System.out.println(user.getName() + " " + monthlyReadings.size() + " readings for month " + month);
            for (MeterReading reading : monthlyReadings) {
                System.out.println(reading);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles request to fetch all readings for a user.
     *
     * @param user the user for whom the readings history is retrieved
     */
    public void handleGetReadingsHistoryRequest(User user) {
        try {
            List<MeterReading> readingsHistory = meterService.getReadingsHistory(user);
            System.out.println(user.getName() + " " + readingsHistory.size() + " readings in history");
            for (MeterReading reading : readingsHistory) {
                System.out.println(reading);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles request to fetch all readings for all users (for admin user).
     *
     * @param adminUser the admin user requesting for all readings
     */
    public void handleGetAllReadingsRequest(User adminUser) {
        try {
            if (!userService.checkRole(adminUser)) {
                System.out.println("Error: User does not have the necessary permissions to access all readings");
                return;
            }
            List<MeterReading> allReadings = meterService.getAllReadingsHistory(adminUser);
            System.out.println("All readings across all users:");
            for (MeterReading reading : allReadings) {
                System.out.println(reading);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
