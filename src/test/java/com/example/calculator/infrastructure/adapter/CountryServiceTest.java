package com.example.calculator.infrastructure.adapter;

import com.example.calculator.domain.model.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryServiceTest {

    private CountryService countryService;

    @BeforeEach
    void setUp() {
        countryService = new CountryService(new ObjectMapper());
        countryService.loadCountries();
    }

    @Test
    @DisplayName("Should load all EU countries")
    void shouldLoadAllCountries() {
        assertNotNull(countryService.getAllCountries());
        assertFalse(countryService.getAllCountries().isEmpty());
    }

    @Test
    @DisplayName("Should find country by valid code")
    void shouldFindCountryByCode() {
        Country country = countryService.findByCode("DE");
        
        assertNotNull(country);
        assertEquals("DE", country.getCode());
        assertEquals("Germany", country.getName());
    }

    @Test
    @DisplayName("Should return null for invalid country code")
    void shouldReturnNullForInvalidCode() {
        Country country = countryService.findByCode("XX");
        
        assertNull(country);
    }

    @Test
    @DisplayName("Should find France by code")
    void shouldFindFrance() {
        Country country = countryService.findByCode("FR");
        
        assertNotNull(country);
        assertEquals("FR", country.getCode());
        assertEquals("France", country.getName());
    }
}
