package com.ylab.app.mapper;

import com.ylab.app.model.MeterReadingDetails;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * MeterReadingDetailsMapper class
 *
 * @author HP
 * @since 09.02.2024
 */
@Mapper
public interface MeterReadingDetailsMapper {
    /**
     * The constant INSTANCE.
     */
    MeterReadingDetailsMapper INSTANCE = Mappers.getMapper(MeterReadingDetailsMapper.class);

    /**
     * Meter reading details to meter reading details dto meter reading details dto.
     *
     * @param meterReadingDetails the meter reading details
     * @return the meter reading details dto
     */
    @Mappings({
            @Mapping(target = "meterReadingId", source = "meterReadingId"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "value", source = "value")
    })
    MeterReadingDetailsDto meterReadingDetailsToMeterReadingDetailsDto(MeterReadingDetails meterReadingDetails);

    /**
     * Meter reading details dto to meter reading details meter reading details.
     *
     * @param meterReadingDetailsDto the meter reading details dto
     * @return the meter reading details
     */
    @Mappings({
            @Mapping(target = "meterReadingId", source = "meterReadingId"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "value", source = "value")
    })
    MeterReadingDetails meterReadingDetailsDtoToMeterReadingDetails(MeterReadingDetailsDto meterReadingDetailsDto);
}
