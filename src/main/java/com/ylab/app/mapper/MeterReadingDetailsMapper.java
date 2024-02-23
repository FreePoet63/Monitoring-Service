package com.ylab.app.mapper;

import com.ylab.app.model.MeterReadingDetails;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * MeterReadingDetailsMapper interface for mapping between MeterReadingDetails and MeterReadingDetailsDto objects.
 * This interface is used as a component in a Spring application and is responsible for mapping entities to DTOs and vice versa.
 *
 * @author HP
 * @since 09.02.2024
 */
@Mapper(componentModel = "spring")
public interface MeterReadingDetailsMapper {
    /**
     * Maps a MeterReadingDetails object to a MeterReadingDetailsDto object.
     *
     * @param meterReadingDetails the MeterReadingDetails object to map
     * @return the mapped MeterReadingDetailsDto object
     */
    @Mappings({
            @Mapping(target = "meterReadingId", source = "meterReadingId"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "value", source = "value")
    })
    MeterReadingDetailsDto meterReadingDetailsToMeterReadingDetailsDto(MeterReadingDetails meterReadingDetails);

    /**
     * Maps a MeterReadingDetailsDto object to a MeterReadingDetails object.
     *
     * @param meterReadingDetailsDto the MeterReadingDetailsDto object to map
     * @return the mapped MeterReadingDetails object
     */
    @Mappings({
            @Mapping(target = "meterReadingId", source = "meterReadingId"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "value", source = "value")
    })
    MeterReadingDetails meterReadingDetailsDtoToMeterReadingDetails(MeterReadingDetailsDto meterReadingDetailsDto);
}
