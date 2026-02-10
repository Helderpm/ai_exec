package com.example.calculator.application.service;

import com.example.calculator.domain.model.Country;
import com.example.calculator.domain.model.ValidationResult;
import com.example.calculator.domain.model.WorkingDayResult;
import com.example.calculator.domain.model.WorkingDayResultBuilder;
import com.example.calculator.domain.port.CountryRepository;
import com.example.calculator.domain.port.RequestValidator;
import com.example.calculator.domain.port.WorkingDayCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Application service that orchestrates working day calculation use cases.
 * Acts as a facade between the presentation layer and domain layer.
 */
@Service
@RequiredArgsConstructor
public class WorkingDayApplicationService {

    private final WorkingDayCalculator calculator;
    private final RequestValidator validator;
    private final CountryRepository countryRepository;

    /**
     * Validates a working day calculation request.
     *
     * @param start the start date
     * @param end the end date
     * @param countryCode the country code
     * @return validation result containing success or error information
     */
    public ValidationResult validateRequest(LocalDate start, LocalDate end, String countryCode) {
        return validator.validate(start, end, countryCode);
    }

    /**
     * Calculates working days between two dates for a specific country.
     *
     * @param start the start date (inclusive)
     * @param end the end date (inclusive)
     * @param countryCode the country code for holiday calculation
     * @param country the validated country object
     * @return complete result with working days count and metadata
     */
    public WorkingDayResult calculateWorkingDays(LocalDate start, LocalDate end, String countryCode, Country country) {
        long workingDays = calculator.calculateWorkingDays(start, end, countryCode);
        List<Country> allCountries = countryRepository.getAllCountries();

        return new WorkingDayResultBuilder()
                .withWorkingDays(workingDays)
                .withDateRange(start, end)
                .withCountry(country)
                .withAllCountries(allCountries)
                .withSelectedCountryCode(countryCode)
                .build();
    }

    /**
     * Retrieves all available countries.
     *
     * @return list of all countries
     */
    public List<Country> getAllCountries() {
        return countryRepository.getAllCountries();
    }
}
