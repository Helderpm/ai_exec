package com.example.calculator.domain.model;

import java.time.LocalDate;
import java.util.List;

public record WorkingDayResult(
    long workingDays,
    LocalDate startDate,
    LocalDate endDate,
    Country country,
    List<Country> allCountries,
    String selectedCountryCode
) {}
