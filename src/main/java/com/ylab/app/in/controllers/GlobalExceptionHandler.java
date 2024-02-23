package com.ylab.app.in.controllers;

import com.ylab.app.exception.meterException.MeterReadingException;
import com.ylab.app.exception.userException.UserValidationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

/**
 * GlobalExceptionHandler class is a controller advice that handles exceptions globally within the application.
 * It provides methods for handling validation exceptions and data access exceptions.
 *
 * @author razlivinsky
 * @since 16.02.2024
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles validation exceptions and returns a response entity with a bad request status code.
     * @param ex the validation exception
     * @return the response entity with the error message
     */
    @ExceptionHandler({UserValidationException.class, MeterReadingException.class})
    public ResponseEntity<String> handleValidationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles data access exceptions and returns a response entity with an internal server error status code.
     * @param ex the data access exception
     * @return the response entity with the error message
     */
    @ExceptionHandler({DataAccessException.class, SQLException.class})
    public ResponseEntity<String> handleDataAccessException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error: " + ex.getMessage());
    }
}
