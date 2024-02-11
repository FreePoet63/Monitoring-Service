package com.ylab.app.mapper;

import com.ylab.app.model.MeterReading;
import com.ylab.app.model.dto.MeterReadingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * MeterReadingMapper class
 *
 * @author HP
 * @since 09.02.2024
 */
@Mapper(uses = {MeterReadingDetailsMapper.class})
public interface MeterReadingMapper {
    /**
     * The constant INSTANCE.
     */
    MeterReadingMapper INSTANCE = Mappers.getMapper(MeterReadingMapper.class);

    /**
     * Meter reading to meter reading dto meter reading dto.
     *
     * @param meterReading the meter reading
     * @return the meter reading dto
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
     * Meter reading dto to meter reading meter reading.
     *
     * @param meterReadingDto the meter reading dto
     * @return the meter reading
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
