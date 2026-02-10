package com.example.calculator.infrastructure.adapter;

import com.example.calculator.domain.model.Country;
import com.example.calculator.domain.exception.WorkingDayException;
import com.example.calculator.domain.port.CountryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Infrastructure adapter for country data access.
 * Loads country data from JSON file.
 */
@Service
@RequiredArgsConstructor
public class CountryService implements CountryRepository {
    private List<Country> countries;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void loadCountries() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("docs/eu-countries.json")) {
            if (inputStream == null) {
                throw new WorkingDayException("EU countries file not found");
            }
            countries = objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new WorkingDayException("Failed to load EU countries", e);
        }
    }

    @Override
    public List<Country> getAllCountries() {
        return countries;
    }

    @Override
    public Country findByCode(String code) {
        return countries.stream()
                .filter(country -> country.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
