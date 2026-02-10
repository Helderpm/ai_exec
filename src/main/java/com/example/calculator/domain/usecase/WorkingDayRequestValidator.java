package com.example.calculator.domain.usecase;

import com.example.calculator.domain.model.Country;
import com.example.calculator.domain.model.ValidationResult;
import com.example.calculator.domain.model.WorkingDayError;
import com.example.calculator.domain.port.CountryRepository;
import com.example.calculator.domain.port.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

/**
 * Validator for working day calculation requests.
 * Validates date ranges and country codes.
 */
@Component
@RequiredArgsConstructor
public class WorkingDayRequestValidator implements RequestValidator {

    private final CountryRepository countryRepository;

    /**
     * Validates a working day calculation request.
     * Checks date range validity and country code existence.
     *
     * @param start the start date
     * @param end the end date
     * @param countryCode the country code
     * @return validation result with success or error
     */
    @Override
    public ValidationResult validate(LocalDate start, LocalDate end, String countryCode) {
        if (start.isAfter(end)) {
            return ValidationResult.error(WorkingDayError.INVALID_DATE_RANGE);
        }
        
        if (start.isBefore(LocalDate.of(1900, 1, 1)) || end.isAfter(LocalDate.of(2100, 12, 31))) {
            return ValidationResult.error(WorkingDayError.DATE_BEFORE_MINIMUM);
        }

        Country selectedCountry = countryRepository.findByCode(countryCode);
        if (selectedCountry == null) {
            return ValidationResult.error(WorkingDayError.INVALID_COUNTRY);
        }

        return ValidationResult.success(selectedCountry);
    }
}
