package com.ylab.app.model;

import java.util.*;

/**
 * Represents a user in the system.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class User {
    private String name;
    private String password;
    private String role;
    private List<MeterReading> readings;

    /**
     * Instantiates a new User with the specified name, password, and role.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @param role     the role of the user
     */
    public User(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.readings = new ArrayList<>();
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name of the user to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password of the user to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the role of the user to be set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the list of meter readings associated with the user.
     *
     * @return the list of meter readings
     */
    public List<MeterReading> getReadings() {
        return readings;
    }

    /**
     * Sets the list of meter readings associated with the user.
     *
     * @param readings the list of meter readings to be set
     */
    public void setReadings(List<MeterReading> readings) {
        this.readings = readings;
    }

    /**
     * Adds a meter reading to the user's readings list.
     *
     * @param reading the meter reading to be added
     */
    public void addReading(MeterReading reading) {
        this.readings.add(reading);
    }

    /**
     * Generates a string representation of the user.
     *
     * @return a string with the user details
     */
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", readings=" + readings +
                '}';
    }
}