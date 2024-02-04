package com.ylab.app.service;

import com.ylab.app.model.*;

import java.util.List;

/**
 * WebService interface for handling user requests.
 *
 * @author razlivinsky
 * @since 28.01.2024
 */
public interface WebService {
    /**
     * Handles registration request for a new user.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @param role     the role of the user
     */
    public void handleRegisterRequest(String name, String password, UserRole role);

    /**
     * Handles login request for a user.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @return the user object if login is successful, null otherwise
     */
    public User handleLoginRequest(String name, String password);

    /**
     * Handles request to fetch current readings for a user.
     *
     * @param user the user for whom current readings are requested
     */
    public void handleGetCurrentReadingsRequest(User user);

    /**
     * Handles request to submit a new reading for a user's meter.
     *
     * @param user        the user submitting the reading
     * @param numberMeter the meter number for which the reading is being submitted
     * @param readingDetails        the type of the reading
     */
    public void handleSubmitReadingRequest(User user, String numberMeter, List<MeterReadingDetails> readingDetails);

    /**
     * Handles request to fetch readings for a user and a specified month.
     *
     * @param user  the user for whom the readings are requested
     * @param month the month for which the readings are requested
     */
    public void handleGetReadingsByMonthRequest(User user, int month);

    /**
     * Handle get readings history request.
     *
     * @param user the user
     */
    public void handleGetReadingsHistoryRequest(User user);

    /**
     * Handles request to fetch all readings for all users (for admin user).
     *
     * @param adminUser the admin user requesting for all readings
     */
    public void handleGetAllReadingsRequest(User adminUser);
}
