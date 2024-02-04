package com.ylab.app.model;

/**
 * The MeterReadingDetails class represents details of a meter reading.
 *
 * @author razlivinsky
 * @since 03.02.2024
 */
public class MeterReadingDetails {
    private Long meterReadingId;
    private String type;
    private Double value;

    /**
     * Constructs a new MeterReadingDetails object with the specified details.
     *
     * @param meterReadingId the meter reading id
     * @param type           the type of meter reading
     * @param value          the value of the reading
     */
    public MeterReadingDetails(Long meterReadingId, String type, Double value) {
        this.meterReadingId = meterReadingId;
        this.type = type;
        this.value = value;
    }

    /**
     * Constructs a new MeterReadingDetails object with the specified details.
     *
     * @param type  the type of meter reading
     * @param value the value of the reading
     */
    public MeterReadingDetails(String type, Double value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the meter reading id.
     *
     * @return the meter reading id
     */
    public Long getMeterReadingId() {
        return meterReadingId;
    }

    /**
     * Sets the meter reading id.
     *
     * @param meterReadingId the meter reading id
     */
    public void setMeterReadingId(Long meterReadingId) {
        this.meterReadingId = meterReadingId;
    }

    /**
     * Gets the type of reading.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of reading.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the value of the reading.
     *
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value of the reading.
     *
     * @param value the value
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Returns a string representation of the MeterReadingDetails object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "MeterReadingDetails{" +
                "meterReadingId=" + meterReadingId +
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
