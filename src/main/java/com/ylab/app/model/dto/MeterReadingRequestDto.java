package com.ylab.app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * MeterReadingRequestDto class
 *
 * @author HP
 * @since 10.02.2024
 */
public class MeterReadingRequestDto {
    @JsonProperty("number_meter")
    private String numberMeter;
    private List<MeterReadingDetailsDto> detailsList;

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
}
