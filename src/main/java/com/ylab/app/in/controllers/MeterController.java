package com.ylab.app.in.controllers;

import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.User;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.impl.MeterServiceImpl;
import com.ylab.app.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The MeterController class handles HTTP requests related to meter readings.
 * It provides methods for getting current meter readings, submitting new meter readings, and retrieving meter reading history.
 *
 * @RestController indicates that the data returned by each method will be written straight into the response body instead of rendering a template.
 * @RequestMapping provides a mapping from the web request @RequestMapping("/meter-readings").
 * @Author razlivinsky
 * @since 14.02.2024
 */
@RestController
@RequestMapping("/meter-readings")
public class MeterController {
    private final MeterServiceImpl meterService;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    /**
     * Instantiates a new Meter controller.
     * @param meterService the meter service
     * @param userService the user service
     * @param userMapper the user mapper
     */
    public MeterController(MeterServiceImpl meterService, UserServiceImpl userService, UserMapper userMapper) {
        this.meterService = meterService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Retrieves the current meter readings for the authenticated user.
     *
     * @param principal the authenticated user
     * @return a response entity containing a list of current meter readings
     */
    @GetMapping("/current")
    @Operation(summary = "Get Current Reading", description = "method provide current meter reading", tags = {"meters"})
    public ResponseEntity<List<MeterReadingDto>> getCurrentMeterReadings(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        UserDto userDto = userService.getUserByLogin(principal.getUsername());
        User user = userMapper.userDtoToUser(userDto);
        List<MeterReadingDto> currentReadings = meterService.getCurrentReadings(user);
        return ResponseEntity.ok(currentReadings);
    }


    /**
     * Submits a new meter reading for the authenticated user.
     *
     * @param principal the authenticated user
     * @param meterReadingDto the meter reading to submit
     * @return a response entity containing the submitted meter reading
     */
    @PostMapping("/submit")
    @Operation(summary = "Create Meter Reading", description = "method provide create mrter reading", tags = {"meters"})
    public ResponseEntity<MeterReadingDto> submitMeterReading(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
            @RequestBody MeterReadingDto meterReadingDto) {
        UserDto userDto = userService.getUserByLogin(principal.getUsername());
        User user = userMapper.userDtoToUser(userDto);
        String numberMeter = meterReadingDto.getNumberMeter();
        List<MeterReadingDetailsDto> readings = meterReadingDto.getDetailsList();
        MeterReadingDto submittedReading = meterService.submitReading(user, numberMeter, readings);
        return ResponseEntity.status(HttpStatus.CREATED).body(submittedReading);
    }

    /**
     * Retrieves the meter reading history for the authenticated user.
     *
     * @param principal the authenticated user
     * @return a response entity containing the meter reading history for the user
     */
    @GetMapping("/history")
    @Operation(summary = "Get History Readings Valid User", description = "method provide readings history of valid user", tags = {"meters"})
    public ResponseEntity<List<MeterReadingDto>> getMeterReadingHistory(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        UserDto userDto = userService.getUserByLogin(principal.getUsername());
        User user = userMapper.userDtoToUser(userDto);
        List<MeterReadingDto> readingHistory = meterService.getReadingsHistory(user);
        return ResponseEntity.ok(readingHistory);
    }

    /**
     * Retrieves the meter readings for the authenticated user for the specified month.
     *
     * @param principal the authenticated user
     * @param month the month for which to retrieve the meter readings
     * @return a response entity containing the meter readings for the specified month
     */
    @GetMapping("/month/{month}")
    @Operation(summary = "Get Readings By Month", description = "method provide readings by month of valid user", tags = {"meters"})
    public ResponseEntity<List<MeterReadingDto>> getMeterReadingByMonth(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
            @PathVariable int month) {
        UserDto userDto = userService.getUserByLogin(principal.getUsername());
        User user = userMapper.userDtoToUser(userDto);
        List<MeterReadingDto> readingHistory = meterService.getReadingsByMonth(user, month);
        return ResponseEntity.ok(readingHistory);
    }

    /**
     * Retrieves all meter reading history for the authenticated user.
     *
     * @param principal the authenticated user
     * @return a response entity containing all meter reading history for the user
     */
    @GetMapping("/history/all")
    @Operation(summary = "Find All History Readings", description = "method provide get readings all history by admin", tags = {"meters"})
    public ResponseEntity<List<MeterReadingDto>> getMeterReadingAllHistory(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        UserDto userDto = userService.getUserByLogin(principal.getUsername());
        User user = userMapper.userDtoToUser(userDto);
        List<MeterReadingDto> readingHistory = meterService.getAllReadingsHistory(user);
        return ResponseEntity.ok(readingHistory);
    }
}
