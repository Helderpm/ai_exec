package com.example.calculator.infrastructure.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkingDayControllerIT {

@Autowired
private MockMvc mockMvc;

    @Test
    void shouldReturnCalculationResultToView() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2023-10-02")
                        .param("end", "2023-10-04")
                        .param("country", "DE"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("result", 2L))
                .andExpect(model().attribute("selectedCountry", "DE"))
                .andExpect(model().attributeExists("country"))
                .andExpect(model().attribute("countries", not(empty())));
    }

    @Test
    @DisplayName("Should redirect to home when an invalid country code is provided")
    void shouldHandleInvalidCountryCode() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2023-10-02")
                        .param("end", "2023-10-04")
                        .param("country", "INVALID_CODE")) // Fake code
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Invalid country selected"))
                .andExpect(redirectedUrl("/"));
    }
}
