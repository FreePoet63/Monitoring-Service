package com.ylab.app.model;

import java.time.*;
import java.util.*;

/**
 * Represents a meter reading.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class MeterReading {
    private String numberMeter;
    private static Map<String, List<Double>> globalMap = new HashMap<>();
    private Map<String, List<Double>> map;
    private LocalDateTime date;
    private String user;

    /**
     * Instantiates a new Meter reading.
     *
     * @param numberMeter the number meter
     * @param date        the date
     * @param user        the user
     */
    public MeterReading(String numberMeter, LocalDateTime date, String user) {
        this.numberMeter = numberMeter;
        this.date = date;
        this.map = new HashMap<>();
        this.user = user;
    }

    /**
     * Gets the user associated with the meter reading.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the user associated with the meter reading.
     *
     * @param user the user to be set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets number meter.
     *
     * @return the number meter
     */
    public String getNumberMeter() {
        return numberMeter;
    }

    /**
     * Sets number meter.
     *
     * @param numberMeter the meter number to be set
     */
    public void setNumberMeter(String numberMeter) {
        this.numberMeter = numberMeter;
    }

    /**
     * Gets the map of readings.
     *
     * @return the map of readings
     */
    public Map<String, List<Double>> getMap() {
        return map;
    }

    /**
     * Sets the map of readings.
     *
     * @param map the map of readings to be set
     */
    public void setMap(Map<String, List<Double>> map) {
        this.map = map;
    }

    /**
     * Gets the date of the meter reading.
     *
     * @return the date of the meter reading
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date of the meter reading.
     *
     * @param date the date of the meter reading to be set
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Gets the global map of readings across all meter readings.
     *
     * @return the global map of readings
     */
    public static Map<String, List<Double>> getGlobalMap() {
        return globalMap;
    }

    /**
     * Sets the global map of readings.
     *
     * @param globalMap the global map of readings to be set
     */
    public static void setGlobalMap(Map<String, List<Double>> globalMap) {
        MeterReading.globalMap = globalMap;
    }

    /**
     * Adds a reading for a specific type to the meter reading.
     *
     * @param type    the type of reading
     * @param reading the reading value
     */
    public void addReading(String type, double reading) {
        map.computeIfAbsent(type, k -> new ArrayList<>()).add(reading);
        globalMap.computeIfAbsent(type, k -> new ArrayList<>()).add(reading);
    }

    /**
     * Gets the total reading for a specific type.
     *
     * @param type the type of reading
     * @return the total reading for the specified type
     */
    public double getTotalReadingByType(String type) {
        return map.getOrDefault(type, new ArrayList<>()).stream().mapToDouble(Double::doubleValue).sum();
    }

    /**
     * Gets the global total reading for a specific type across all meter readings.
     *
     * @param type the type of reading
     * @return the global total reading for the specified type
     */
    public static double getGlobalTotalReadingByType(String type) {
        return globalMap.getOrDefault(type, new ArrayList<>()).stream().mapToDouble(Double::doubleValue).sum();
    }

    /**
     * Generates a string representation of the meter reading.
     *
     * @return a string with the meter reading details
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Meter reading №").append(numberMeter).append("\n");
        sb.append("Date: ").append(date).append("\n");
        for (Map.Entry<String, List<Double>> entry : map.entrySet()) {
            String type = entry.getKey();
            double totalByType = getTotalReadingByType(type);
            double globalTotalByType = getGlobalTotalReadingByType(type);
            sb.append(type).append(": ").append(totalByType)
                    .append(" (Total: ").append(globalTotalByType).append(")\n");
        }
        sb.append(user);
        return sb.toString();
    }
}

