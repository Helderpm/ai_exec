package com.example.calculator.application.service;

import com.example.calculator.domain.model.Country;
import com.example.calculator.domain.model.ValidationResult;
import com.example.calculator.domain.model.WorkingDayResult;
import com.example.calculator.domain.model.WorkingDayError;
import com.example.calculator.domain.port.CountryRepository;
import com.example.calculator.domain.port.RequestValidator;
import com.example.calculator.domain.port.WorkingDayCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkingDayApplicationServiceTest {

    @Mock
    private WorkingDayCalculator calculator;

    @Mock
    private RequestValidator validator;

    @Mock
    private CountryRepository countryRepository;

    private WorkingDayApplicationService service;

    @BeforeEach
    void setUp() {
        service = new WorkingDayApplicationService(calculator, validator, countryRepository);
    }

    @Test
    @DisplayName("Should validate request successfully")
    void shouldValidateRequest() {
        LocalDate start = LocalDate.of(2023, 10, 5);
        LocalDate end = LocalDate.of(2023, 10, 10);
        Country germany = new Country("DE", "Germany");
        ValidationResult expected = ValidationResult.success(germany);

        when(validator.validate(start, end, "DE")).thenReturn(expected);

        ValidationResult result = service.validateRequest(start, end, "DE");

        assertTrue(result.valid());
        assertEquals(germany, result.country());
        verify(validator).validate(start, end, "DE");
    }

    @Test
    @DisplayName("Should return validation error")
    void shouldReturnValidationError() {
        LocalDate start = LocalDate.of(2023, 10, 10);
        LocalDate end = LocalDate.of(2023, 10, 5);
        ValidationResult expected = ValidationResult.error(WorkingDayError.INVALID_DATE_RANGE);

        when(validator.validate(start, end, "DE")).thenReturn(expected);

        ValidationResult result = service.validateRequest(start, end, "DE");

        assertFalse(result.valid());
        assertEquals(WorkingDayError.INVALID_DATE_RANGE, result.error());
    }

    @Test
    @DisplayName("Should calculate working days and build result")
    void shouldCalculateWorkingDays() {
        LocalDate start = LocalDate.of(2023, 10, 2);
        LocalDate end = LocalDate.of(2023, 10, 6);
        Country germany = new Country("DE", "Germany");
        List<Country> countries = List.of(germany);

        when(calculator.calculateWorkingDays(start, end, "DE")).thenReturn(4L);
        when(countryRepository.getAllCountries()).thenReturn(countries);

        WorkingDayResult result = service.calculateWorkingDays(start, end, "DE", germany);

        assertEquals(4L, result.workingDays());
        assertEquals(start, result.startDate());
        assertEquals(end, result.endDate());
        assertEquals(germany, result.country());
        assertEquals(countries, result.allCountries());
        assertEquals("DE", result.selectedCountryCode());
        verify(calculator).calculateWorkingDays(start, end, "DE");
        verify(countryRepository).getAllCountries();
    }

    @Test
    @DisplayName("Should get all countries")
    void shouldGetAllCountries() {
        List<Country> countries = List.of(
            new Country("DE", "Germany"),
            new Country("FR", "France")
        );

        when(countryRepository.getAllCountries()).thenReturn(countries);

        List<Country> result = service.getAllCountries();

        assertEquals(2, result.size());
        assertEquals(countries, result);
        verify(countryRepository).getAllCountries();
    }
}
