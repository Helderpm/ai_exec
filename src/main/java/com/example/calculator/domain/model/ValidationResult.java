package com.example.calculator.domain.model;

/**
 * Result of request validation containing success status, error, and validated country.
 *
 * @param valid true if validation passed
 * @param error validation error if validation failed
 * @param country validated country if validation passed
 */
public record ValidationResult(boolean valid, WorkingDayError error, Country country) {
    
    public static ValidationResult success(Country country) {
        return new ValidationResult(true, null, country);
    }
    
    public static ValidationResult error(WorkingDayError error) {
        return new ValidationResult(false, error, null);
    }
}
