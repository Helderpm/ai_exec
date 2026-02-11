package com.example.calculator.domain.port;

import com.example.calculator.domain.model.ValidationResult;
import java.time.LocalDate;

public interface RequestValidator {
    ValidationResult validate(LocalDate start, LocalDate end, String countryCode);
}
