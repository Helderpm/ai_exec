package com.example.calculator.domain.port;

import java.time.LocalDate;

public interface WorkingDayCalculator {
    long calculateWorkingDays(LocalDate start, LocalDate end, String countryCode);
}
