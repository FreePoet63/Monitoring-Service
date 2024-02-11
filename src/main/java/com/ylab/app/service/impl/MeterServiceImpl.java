package com.ylab.app.service.impl;

import com.ylab.app.dbService.dao.MeterReadingDao;
import com.ylab.app.dbService.dao.impl.MeterReadingDaoImpl;

import com.ylab.app.exception.meterException.MeterReadingException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.MeterReading;
import com.ylab.app.model.MeterReadingDetails;
import com.ylab.app.model.Session;
import com.ylab.app.model.User;
import com.ylab.app.service.MeterService;
import com.ylab.app.service.UserService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MeterServiceImpl class for managing meter readings.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class MeterServiceImpl implements MeterService {
    private UserService userService;
    private MeterReadingDao readingDao;

    /**
     * Instantiates a new Meter service.
     *
     * @param userService the user service to be used
     */
    public MeterServiceImpl(UserService userService) {
        this.userService = userService;
        this.readingDao = new MeterReadingDaoImpl();

    }

    /**
     * Gets the current readings for the specified user.
     *
     * @param user the user for whom the current readings are retrieved
     * @return the list of current readings
     * @throws UserValidationException if the user is invalid
     */
    public List<MeterReading> getCurrentReadings(User user) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        return readingDao.selectCurrentMaterReading(user);
    }

    /**
     * Submits a new reading for the specified user and meter.
     *
     * @param user        the user submitting the reading
     * @param numberMeter the meter number for the readings
     * @param  readings list the values of the reading
     * @throws MeterReadingException if the meter number, type, or value is invalid
     */
    public void submitReading(User user, String numberMeter, List<MeterReadingDetails> readings) {
        Session session = Session.getInstance();
        user = session.getUser();

        if (numberMeter == null || numberMeter.isEmpty() || numberMeter.contains(" ")) {
            throw new MeterReadingException("Invalid numberMeter");
        }
        try {
            MeterReading meterReading = new MeterReading(numberMeter, LocalDateTime.now(), user);
            meterReading.setUser(user);

            for (MeterReadingDetails reading : readings) {
                meterReading.addReadingDetails(reading.getType(), reading.getValue());
            }
            readingDao.insertMeterReading(meterReading);
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
    public List<MeterReading> getReadingsByMonth(User user, int month) {
        Session session = Session.getInstance();
        user = session.getUser();
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (month < Month.JANUARY.getValue() || month > Month.DECEMBER.getValue()) {
            throw new MeterReadingException("Invalid month");
        }
        List<MeterReading> userReadings = readingDao.selectByNameUser(user);
        return userReadings.stream()
                .filter(reading -> reading.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    /**
     * Gets the readings history for the specified user.
     *
     * @param user the user for whom the readings history is retrieved
     * @return the list of readings history for the specified user
     * @throws UserValidationException if the user is invalid
     */
    public List<MeterReading> getReadingsHistory(User user) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        return readingDao.selectByNameUser(user);
    }

    /**
     * Gets the readings history for all users (for admin user).
     *
     * @param adminUser the admin user retrieving the readings history
     * @return the list of all readings history
     * @throws UserValidationException if the admin user is invalid or unauthorized
     * @throws MeterReadingException if the admin user is invalid or unauthorized
     */
    public List<MeterReading> getAllReadingsHistory(User adminUser) {
        if (adminUser == null || !userService.hasRoleAdmin(adminUser)) {
            throw new UserValidationException("Invalid or unauthorized user");
        }
        try {
            return readingDao.selectByAllMeterReadings();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MeterReadingException("Invalid date " + e.getMessage());
        }
    }
}
