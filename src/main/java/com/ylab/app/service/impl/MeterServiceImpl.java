package com.ylab.app.service.impl;

import com.ylab.app.aspect.LogExecution;
import com.ylab.app.dbService.dao.MeterReadingDao;
import com.ylab.app.dbService.dao.impl.MeterReadingDaoImpl;
import com.ylab.app.exception.meterException.MeterReadingException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.mapper.MeterReadingMapper;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.User;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.service.MeterService;
import com.ylab.app.service.UserService;

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
@LogExecution
public class MeterServiceImpl implements MeterService {
    private UserService userService;
    private MeterReadingDao readingDao;
    private MeterReadingMapper meterReadingMapper;

    /**
     * Instantiates a new Meter service.
     */
    public MeterServiceImpl() {
        this.userService = new UserServiceImpl();
        this.meterReadingMapper = MeterReadingMapper.INSTANCE;
        this.readingDao = new MeterReadingDaoImpl();
    }

    /**
     * Gets the current readings for the specified user.
     *
     * @param user the user for whom the current readings are retrieved
     * @return the list of current readings
     * @throws UserValidationException if the user is invalid
     */
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
     * Submits a new reading for the specified user and meter.
     *
     * @param user        the user submitting the reading
     * @param numberMeter the meter number for the readings
     * @param  readings list the values of the reading
     * @throws MeterReadingException if the meter number, type, or value is invalid
     */
    public MeterReadingDto submitReading(User user, String numberMeter, List<MeterReadingDetailsDto> readings) {
        if (numberMeter == null || numberMeter.isEmpty() || numberMeter.contains(" ")) {
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
        } catch (SQLException e) {
            throw new MeterReadingException("Invalid date " + e.getMessage());
        }
    }

    /**
     * Gets the readings for the specified user and month.
     *
     * @param user  the user for whom the readings are retrieved
     * @param month the month for which readings are retrieved
     * @return the list of readings for the specified month
     * @throws UserValidationException if the user is invalid or the month is out of range
     * @throws MeterReadingException if the user is invalid or the month is out of range
     */
    public List<MeterReadingDto> getReadingsByMonth(User user, int month) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (month < Month.JANUARY.getValue() || month > Month.DECEMBER.getValue()) {
            throw new MeterReadingException("Invalid month");
        }
        List<MeterReading> userReadings = readingDao.selectByNameUser(user);
        return userReadings.stream()
                .map(meterReadingMapper::meterReadingToMeterReadingDto)
                .filter(readingDto -> readingDto.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    /**
     * Gets the readings history for the specified user.
     *
     * @param user the user for whom the readings history is retrieved
     * @return the list of readings history for the specified user
     * @throws UserValidationException if the user is invalid
     */
    public List<MeterReadingDto> getReadingsHistory(User user) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        List<MeterReading> userReadings = readingDao.selectByNameUser(user);
        return userReadings.stream()
                .map(meterReadingMapper::meterReadingToMeterReadingDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets the readings history for all users (for admin user).
     *
     * @param adminUser the admin user retrieving the readings history
     * @return the list of all readings history
     * @throws UserValidationException if the admin user is invalid or unauthorized
     * @throws MeterReadingException if the admin user is invalid or unauthorized
     */
    public List<MeterReadingDto> getAllReadingsHistory(User adminUser) {
        if (adminUser == null || !userService.hasRoleAdmin(adminUser)) {
            throw new UserValidationException("Invalid or unauthorized user");
        }

        try {
            List<MeterReading> allMeterReadings = readingDao.selectByAllMeterReadings();
            return allMeterReadings.stream()
                    .map(meterReadingMapper::meterReadingToMeterReadingDto)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MeterReadingException("Invalid date " + e.getMessage());
        }
    }
}

