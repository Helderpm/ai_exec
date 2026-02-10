package com.example.calculator.domain.usecase;

import com.example.calculator.domain.model.Country;
import com.example.calculator.domain.model.ValidationResult;
import com.example.calculator.domain.model.WorkingDayError;
import com.example.calculator.domain.port.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkingDayRequestValidatorTest {

    @Mock
    private CountryRepository countryRepository;

    private WorkingDayRequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new WorkingDayRequestValidator(countryRepository);
    }

    @Test
    @DisplayName("Should return error when start date is after end date")
    void shouldReturnErrorWhenStartAfterEnd() {
        LocalDate start = LocalDate.of(2023, 10, 10);
        LocalDate end = LocalDate.of(2023, 10, 5);

        ValidationResult result = validator.validate(start, end, "DE");

        assertFalse(result.valid());
        assertEquals(WorkingDayError.INVALID_DATE_RANGE, result.error());
    }

    @Test
    @DisplayName("Should return error when start date is before minimum")
    void shouldReturnErrorWhenStartBeforeMinimum() {
        LocalDate start = LocalDate.of(1899, 12, 31);
        LocalDate end = LocalDate.of(2023, 10, 5);

        ValidationResult result = validator.validate(start, end, "DE");

        assertFalse(result.valid());
        assertEquals(WorkingDayError.DATE_BEFORE_MINIMUM, result.error());
    }

    @Test
    @DisplayName("Should return error when end date is after maximum")
    void shouldReturnErrorWhenEndAfterMaximum() {
        LocalDate start = LocalDate.of(2023, 10, 5);
        LocalDate end = LocalDate.of(2101, 1, 1);

        ValidationResult result = validator.validate(start, end, "DE");

        assertFalse(result.valid());
        assertEquals(WorkingDayError.DATE_BEFORE_MINIMUM, result.error());
    }

    @Test
    @DisplayName("Should return error when country code is invalid")
    void shouldReturnErrorWhenCountryInvalid() {
        LocalDate start = LocalDate.of(2023, 10, 5);
        LocalDate end = LocalDate.of(2023, 10, 10);
        
        when(countryRepository.findByCode("XX")).thenReturn(null);

        ValidationResult result = validator.validate(start, end, "XX");

        assertFalse(result.valid());
        assertEquals(WorkingDayError.INVALID_COUNTRY, result.error());
    }

    @Test
    @DisplayName("Should return success for valid input")
    void shouldReturnSuccessForValidInput() {
        LocalDate start = LocalDate.of(2023, 10, 5);
        LocalDate end = LocalDate.of(2023, 10, 10);
        Country germany = new Country("DE", "Germany");
        
        when(countryRepository.findByCode("DE")).thenReturn(germany);

        ValidationResult result = validator.validate(start, end, "DE");

        assertTrue(result.valid());
        assertNull(result.error());
        assertEquals(germany, result.country());
    }
}
