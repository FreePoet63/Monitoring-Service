package com.ylab.app.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MeterReadingDto class
 *
 * @author HP
 * @since 09.02.2024
 */
public class MeterReadingDto {
    private Long id;
    private String numberMeter;
    private LocalDateTime date;
    private User user;
    private List<MeterReadingDetailsDto> detailsList;

    /**
     * Instantiates a new Meter reading dto.
     */
    public MeterReadingDto() {}

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
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
     * @param numberMeter the number meter
     */
    public void setNumberMeter(String numberMeter) {
        this.numberMeter = numberMeter;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets details list.
     *
     * @return the details list
     */
    public List<MeterReadingDetailsDto> getDetailsList() {
        return detailsList;
    }

    /**
     * Sets details list.
     *
     * @param detailsList the details list
     */
    public void setDetailsList(List<MeterReadingDetailsDto> detailsList) {
        this.detailsList = detailsList;
    }

    @Override
    public String toString() {
        return "MeterReadingDto{" +
                "numberMeter=" + numberMeter + '\'' +
                ", date=" + date +
                ", user=" + user +
                ", detailsList=" + detailsList +
                '}';
    }
}