package com.ylab.app.exception.meterException;

/**
 * The MeterReadingException class represents an exception that is thrown for meter reading related errors.
 * It extends the RuntimeException class, making it an unchecked exception.
 *
 * @author razlivinsky
 * @since 03.02.2024
 */
public class MeterReadingException extends RuntimeException{
    /**
     * Constructs a new MeterReadingException with the specified detail message.
     *
     * @param message the detail message
     */
    public MeterReadingException(String message) {
        super(message);
    }

    /**
     * Constructs a new MeterReadingException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public MeterReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}