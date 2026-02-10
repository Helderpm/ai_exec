package com.example.calculator.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkingDayControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should redirect to home when start date is after end date")
    void shouldRedirectWhenStartDateAfterEndDate() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2023-10-10")
                        .param("end", "2023-10-05")
                        .param("country", "DE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", "Start date cannot be after end date"));
    }

    @Test
    @DisplayName("Should redirect to home when start date is before minimum allowed date")
    void shouldRedirectWhenStartDateBeforeMinDate() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "1899-12-31")
                        .param("end", "2023-10-05")
                        .param("country", "DE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", "Date range must be between 1900-01-01 and 2100-12-31"));
    }

    @Test
    @DisplayName("Should redirect to home when end date is after maximum allowed date")
    void shouldRedirectWhenEndDateAfterMaxDate() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2023-10-05")
                        .param("end", "2101-01-01")
                        .param("country", "DE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", "Date range must be between 1900-01-01 and 2100-12-31"));
    }

    @Test
    @DisplayName("Should return 400 when start date is missing")
    void shouldReturnBadRequestWhenStartDateMissing() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("end", "2023-10-05")
                        .param("country", "DE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when end date is missing")
    void shouldReturnBadRequestWhenEndDateMissing() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2023-10-05")
                        .param("country", "DE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when start date has invalid format")
    void shouldReturnBadRequestWhenStartDateInvalidFormat() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "invalid-date")
                        .param("end", "2023-10-05")
                        .param("country", "DE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when end date has invalid format")
    void shouldReturnBadRequestWhenEndDateInvalidFormat() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2023-10-05")
                        .param("end", "invalid-date")
                        .param("country", "DE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should allow valid date range at boundaries")
    void shouldAllowValidDateRangeAtBoundaries() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "1900-01-01")
                        .param("end", "2100-12-31")
                        .param("country", "DE"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("result"));
    }

    @Test
    @DisplayName("Should handle same day calculation at minimum boundary")
    void shouldHandleSameDayAtMinBoundary() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "1900-01-01")
                        .param("end", "1900-01-01")
                        .param("country", "DE"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("result", 0L)); // 1900-01-01 is New Year's Day (holiday)
    }

    @Test
    @DisplayName("Should handle same day calculation at maximum boundary")
    void shouldHandleSameDayAtMaxBoundary() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2100-12-31")
                        .param("end", "2100-12-31")
                        .param("country", "DE"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("result", 1L)); // 2100-12-31 is a Friday
    }

    @Test
    @DisplayName("Should handle calculation with German holidays")
    void shouldHandleCalculationWithGermanHolidays() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2023-10-02")
                        .param("end", "2023-10-06")
                        .param("country", "DE"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("result", 4L)); // 4 days (Oct 3 is German Unity Day)
    }

    @Test
    @DisplayName("Should handle edge case of exactly minimum date")
    void shouldHandleExactlyMinimumDate() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "1900-01-01")
                        .param("end", "1900-01-02")
                        .param("country", "DE"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("result"));
    }

    @Test
    @DisplayName("Should handle edge case of exactly maximum date")
    void shouldHandleExactlyMaximumDate() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2100-12-30")
                        .param("end", "2100-12-31")
                        .param("country", "DE"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("result"));
    }

    @Test
    @DisplayName("Should return 400 for empty date parameters")
    void shouldReturnBadRequestForEmptyDateParameters() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "")
                        .param("end", "")
                        .param("country", "DE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for null-like date parameters")
    void shouldReturnBadRequestForNullLikeParameters() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "null")
                        .param("end", "2023-10-05")
                        .param("country", "DE"))
                .andExpect(status().isBadRequest());
    }
}
