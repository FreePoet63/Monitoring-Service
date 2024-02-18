package com.ylab.app.mapper;

import com.ylab.app.model.MeterReading;
import com.ylab.app.model.dto.MeterReadingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * MeterReadingMapper interface for mapping between MeterReading and MeterReadingDto objects.
 * This interface is used as a component in a Spring application and utilizes the MapStruct library for object mapping.
 *
 * @author HP
 * @since 09.02.2024
 */
@Mapper(componentModel = "spring", uses = {MeterReadingDetailsMapper.class})
public interface MeterReadingMapper {
    /**
     * Maps a MeterReading object to a MeterReadingDto object.
     *
     * @param meterReading the MeterReading object to map
     * @return the mapped MeterReadingDto object
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "numberMeter", source = "numberMeter"),
            @Mapping(target = "date", source = "date"),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "detailsList", source = "detailsList")
    })
    MeterReadingDto meterReadingToMeterReadingDto(MeterReading meterReading);

    /**
     * Maps a MeterReadingDto object to a MeterReading object.
     *
     * @param meterReadingDto the MeterReadingDto object to map
     * @return the mapped MeterReading object
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "numberMeter", source = "numberMeter"),
            @Mapping(target = "date", source = "date"),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "detailsList", source = "detailsList")
    })
    MeterReading meterReadingDtoToMeterReading(MeterReadingDto meterReadingDto);
}
