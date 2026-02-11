package com.example.calculator.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Validation error codes and messages for working day calculations.
 */
@Getter
@RequiredArgsConstructor
public enum WorkingDayError {
    
    INVALID_DATE_RANGE("DATE_001", "Start date cannot be after end date"),
    DATE_BEFORE_MINIMUM("DATE_002", "Date range must be between 1900-01-01 and 2100-12-31"),
    INVALID_COUNTRY("COUNTRY_001", "Invalid country selected");
    
    private final String code;
    private final String message;
}
