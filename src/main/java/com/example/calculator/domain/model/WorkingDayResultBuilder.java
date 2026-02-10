package com.example.calculator.domain.model;

import java.time.LocalDate;
import java.util.List;

public class WorkingDayResultBuilder {
    
    private long workingDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private Country country;
    private List<Country> allCountries;
    private String selectedCountryCode;

    public WorkingDayResultBuilder withWorkingDays(long workingDays) {
        this.workingDays = workingDays;
        return this;
    }

    public WorkingDayResultBuilder withDateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    public WorkingDayResultBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public WorkingDayResultBuilder withAllCountries(List<Country> allCountries) {
        this.allCountries = allCountries;
        return this;
    }

    public WorkingDayResultBuilder withSelectedCountryCode(String selectedCountryCode) {
        this.selectedCountryCode = selectedCountryCode;
        return this;
    }

    public WorkingDayResult build() {
        return new WorkingDayResult(
            workingDays,
            startDate,
            endDate,
            country,
            allCountries,
            selectedCountryCode
        );
    }
}
