package com.ylab.app.model;

import com.ylab.app.dbService.dao.impl.MeterReadingDaoImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a meter reading.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class MeterReading {
    private Long id;
    private String numberMeter;
    private LocalDateTime date;
    private User user;
    private List<MeterReadingDetails> detailsList;

    /**
     * Instantiates a new Meter reading.
     *
     * @param numberMeter the number meter
     * @param date        the date
     * @param user        the user
     */
    public MeterReading(String numberMeter, LocalDateTime date, User user) {
        this.numberMeter = numberMeter;
        this.date = date;
        this.user = user;
        this.detailsList = new ArrayList<>();
    }

    /**
     * Gets id of the meter reading.
     *
     * @return the id of the meter reading
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id of the meter reading.
     *
     * @param id the id of the meter reading to be set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user associated with the meter reading.
     *
     * @return the user of the meter reading
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the meter reading.
     *
     * @param user the user of the meter reading to be set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets number meter of the meter reading.
     *
     * @return the number meter of the meter reading
     */
    public String getNumberMeter() {
        return numberMeter;
    }

    /**
     * Sets number meter of the meter reading.
     *
     * @param numberMeter the meter number to be set
     */
    public void setNumberMeter(String numberMeter) {
        this.numberMeter = numberMeter;
    }

    /**
     * Gets details list of the meter reading.
     *
     * @return the details list of the meter reading
     */
    public List<MeterReadingDetails> getDetailsList() {
        return detailsList;
    }

    /**
     * Sets details list of the meter reading.
     *
     * @param detailsList the details list
     */
    public void setDetailsList(List<MeterReadingDetails> detailsList) {
        this.detailsList = detailsList;
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
     * Add details of the meter reading with the specified type and value.
     *
     * @param type  the type of the reading
     * @param value the value of the reading
     */
    public void addReadingDetails(String type, Double value) {
        MeterReadingDetails details = new MeterReadingDetails(this.id, type, value);
        detailsList.add(details);
    }

    /**
     * Generates a string representation of the meter reading.
     *
     * @return a string with the meter reading details
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Meter reading №").append(numberMeter).append("\n");
        sb.append("Date: ").append(date).append("\n");
        for (MeterReadingDetails details : detailsList) {
            String type = details.getType();
            double totalByType = details.getValue();
            double globalTotalByType = new MeterReadingDaoImpl().findToSumReadingForType(type, user);
            sb.append(type).append(": ").append(totalByType)
                    .append(" (Total: ").append(globalTotalByType).append(")\n");
        }
        sb.append(user);
        return sb.toString();
    }
}

