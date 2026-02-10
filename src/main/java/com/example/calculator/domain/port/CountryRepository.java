package com.example.calculator.domain.port;

import com.example.calculator.domain.model.Country;
import java.util.List;

/**
 * Repository interface for country data access.
 */
public interface CountryRepository {
    /**
     * Retrieves all available countries.
     *
     * @return list of all countries
     */
    List<Country> getAllCountries();
    
    /**
     * Finds a country by its code.
     *
     * @param code the country code
     * @return the country, or null if not found
     */
    Country findByCode(String code);
}
