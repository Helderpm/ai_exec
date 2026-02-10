package com.example.calculator.domain.exception;

/**
 * Domain exception for working day calculation errors.
 */
public class WorkingDayException extends RuntimeException {
    public WorkingDayException(String message) {
        super(message);
    }
    
    public WorkingDayException(String message, Throwable cause) {
        super(message, cause);
    }
}
