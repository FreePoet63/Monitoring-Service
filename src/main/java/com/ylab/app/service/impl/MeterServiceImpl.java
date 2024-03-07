package com.ylab.app.service.impl;

import com.ylab.app.dbService.dao.MeterReadingDao;
import com.ylab.app.exception.meterException.MeterReadingException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.mapper.MeterReadingMapper;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.User;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.service.MeterService;
import com.ylab.app.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MeterServiceImpl class for managing meter readings.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
@Service
public class MeterServiceImpl implements MeterService {
    private final UserService userService;
    private final MeterReadingDao readingDao;
    private final MeterReadingMapper meterReadingMapper;

    /**
     * Instantiates a new Meter service.
     *
     * @param userService        the user service
     * @param readingDao         the meter reading data access object
     * @param meterReadingMapper the meter reading mapper
     */
    public MeterServiceImpl(UserService userService, MeterReadingDao readingDao, MeterReadingMapper meterReadingMapper) {
        this.userService = userService;
        this.readingDao = readingDao;
        this.meterReadingMapper = meterReadingMapper;
    }

    /**
     * Retrieves the current meter readings for a specific user.
     *
     * @param user the user for whom to retrieve the current meter readings
     * @return the list of current meter readings in data transfer object form
     * @throws UserValidationException if the user is invalid
     */
    @Override
    public List<MeterReadingDto> getCurrentReadings(User user) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        List<MeterReading> currentMeterReadings = readingDao.selectCurrentMaterReading(user);
        return currentMeterReadings.stream()
                .map(meterReadingMapper::meterReadingToMeterReadingDto)
                .collect(Collectors.toList());
    }

    /**
     * Submits a new meter reading for a specific user.
     *
     * @param user     the user submitting the meter reading
     * @param numberMeter the meter number for the reading
     * @param readings the details of the meter reading
     * @return the submitted meter reading in data transfer object form
     * @throws MeterReadingException if the meter number is invalid or if there is a failure in inserting the reading
     */
    @Override
    public MeterReadingDto submitReading(User user, String numberMeter, List<MeterReadingDetailsDto> readings) {
        if (numberMeter == null || numberMeter.isEmpty()) {
            throw new MeterReadingException("Invalid numberMeter");
        }
        try {
            MeterReadingDto meterReadingDto = new MeterReadingDto();
            meterReadingDto.setNumberMeter(numberMeter);
            meterReadingDto.setDate(LocalDateTime.now());
            meterReadingDto.setUser(user);

            List<MeterReadingDetailsDto> detailsDtoList = new ArrayList<>();
            for (MeterReadingDetailsDto readingDto : readings) {
                detailsDtoList.add(readingDto);
            }
            meterReadingDto.setDetailsList(detailsDtoList);
            MeterReading meterReading = meterReadingMapper.meterReadingDtoToMeterReading(meterReadingDto);
            readingDao.insertMeterReading(meterReading);
            return meterReadingMapper.meterReadingToMeterReadingDto(meterReading);
        } catch (DataAccessException e) {
            throw new MeterReadingException("Failed to insert meter reading", e);
        }
    }

    /**
     * Retrieves the meter readings for a specific user in a given month.
     *
     * @param user  the user for whom to retrieve the meter readings
     * @param month the month for which to retrieve the meter readings
     * @return the list of meter readings for the specified user and month in data transfer object form
     * @throws UserValidationException if the user is invalid
     * @throws MeterReadingException  if the month is invalid
     */
    @Override
    public List<MeterReadingDto> getReadingsByMonth(User user, int month) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (month < Month.JANUARY.getValue() || month > Month.DECEMBER.getValue()) {
            throw new MeterReadingException("Invalid month");
        }
        List<MeterReading> userReadings = readingDao.selectByUserName(user);
        return userReadings.stream()
                .map(meterReadingMapper::meterReadingToMeterReadingDto)
                .filter(readingDto -> readingDto.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the complete meter reading history for a specific user.
     *
     * @param user the user for whom to retrieve the meter reading history
     * @return the complete meter reading history for the specified user in data transfer object form
     * @throws UserValidationException if the user is invalid
     */
    @Override
    public List<MeterReadingDto> getReadingsHistory(User user) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        List<MeterReading> userReadings = readingDao.selectByUserName(user);
        return userReadings.stream()
                .map(meterReadingMapper::meterReadingToMeterReadingDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the complete meter reading history for all users (accessible to admin users only).
     *
     * @param adminUser the admin user requesting the complete meter reading history
     * @return the complete meter reading history for all users in data transfer object form
     * @throws UserValidationException if the admin user is invalid or unauthorized
     * @throws MeterReadingException  if there is a failure in retrieving the complete meter reading history
     */
    @Override
    public List<MeterReadingDto> getAllReadingsHistory(User adminUser) {
        if (adminUser == null || !userService.hasRoleAdmin(adminUser)) {
            throw new UserValidationException("Invalid or unauthorized user");
        }
        try {
            List<MeterReading> allMeterReadings = readingDao.selectByAllMeterReadings();
            return allMeterReadings.stream()
                    .map(meterReadingMapper::meterReadingToMeterReadingDto)
                    .collect(Collectors.toList());
        } catch (DataAccessException | SQLException e) {
            throw new MeterReadingException("Failed to retrieve all meter readings", e);
        }
    }
}

