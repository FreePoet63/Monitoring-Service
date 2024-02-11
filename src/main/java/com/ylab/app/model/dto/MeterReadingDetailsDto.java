package com.ylab.app.model.dto;

import com.ylab.app.model.MeterReadingDetails;

/**
 * MeterReadingDetailsDto class
 *
 * @author HP
 * @since 09.02.2024
 */
public class MeterReadingDetailsDto {
    private Long meterReadingId;
    private String type;
    private Double value;

    /**
     * Instantiates a new Meter reading details dto.
     */
    public MeterReadingDetailsDto() {
    }

    /**
     * Instantiates a new Meter reading details dto.
     *
     * @param type  the type
     * @param value the value
     */
    public MeterReadingDetailsDto(String type, Double value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets meter reading id.
     *
     * @return the meter reading id
     */
    public Long getMeterReadingId() {
        return meterReadingId;
    }

    /**
     * Sets meter reading id.
     *
     * @param meterReadingId the meter reading id
     */
    public void setMeterReadingId(Long meterReadingId) {
        this.meterReadingId = meterReadingId;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MeterReadingDetailsDto{" +
                "type=" + type + '\'' +
                ", value=" + value +
                '}';
    }
}