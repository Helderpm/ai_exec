package com.example.calculator.infrastructure.web;

import com.example.calculator.domain.WorkingDayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.time.LocalDate;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.*;

class WorkingDayControllerUnitTest {

    private WorkingDayController controller;
    private RedirectAttributes redirectAttributes;
    private Model model;

    @BeforeEach
    void setUp() {
        controller = new WorkingDayController(new WorkingDayService());
        redirectAttributes = new RedirectAttributesModelMap();
        model = new ExtendedModelMap();
    }

    @Test
    @DisplayName("Should redirect with error when start date is after end date")
    void shouldRedirectWhenStartDateAfterEndDate() {
        LocalDate start = LocalDate.of(2023, 10, 10);
        LocalDate end = LocalDate.of(2023, 10, 5);

        String result = controller.calculate(start, end, model, redirectAttributes);

        assertEquals("redirect:/", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("Start date cannot be after end date", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    @DisplayName("Should redirect with error when start date is before minimum allowed")
    void shouldRedirectWhenStartDateBeforeMinDate() {
        LocalDate start = LocalDate.of(1899, 12, 31);
        LocalDate end = LocalDate.of(2023, 10, 5);

        String result = controller.calculate(start, end, model, redirectAttributes);

        assertEquals("redirect:/", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("Date range must be between 1900-01-01 and 2100-12-31", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    @DisplayName("Should redirect with error when end date is after maximum allowed")
    void shouldRedirectWhenEndDateAfterMaxDate() {
        LocalDate start = LocalDate.of(2023, 10, 5);
        LocalDate end = LocalDate.of(2101, 1, 1);

        String result = controller.calculate(start, end, model, redirectAttributes);

        assertEquals("redirect:/", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("Date range must be between 1900-01-01 and 2100-12-31", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    @DisplayName("Should return calculation result for valid date range")
    void shouldReturnCalculationForValidDateRange() {
        LocalDate start = LocalDate.of(2023, 10, 2); // Monday
        LocalDate end = LocalDate.of(2023, 10, 6);   // Friday

        String result = controller.calculate(start, end, model, redirectAttributes);

        assertEquals("index", result);
        assertFalse(redirectAttributes.getFlashAttributes().containsKey("error"));
    }

    @Test
    @DisplayName("Should allow dates at minimum boundary")
    void shouldAllowDatesAtMinBoundary() {
        LocalDate start = LocalDate.of(1900, 1, 1);
        LocalDate end = LocalDate.of(1900, 1, 2);

        String result = controller.calculate(start, end, model, redirectAttributes);

        assertEquals("index", result);
        assertFalse(redirectAttributes.getFlashAttributes().containsKey("error"));
    }

    @Test
    @DisplayName("Should allow dates at maximum boundary")
    void shouldAllowDatesAtMaxBoundary() {
        LocalDate start = LocalDate.of(2100, 12, 30);
        LocalDate end = LocalDate.of(2100, 12, 31);

        String result = controller.calculate(start, end, model, redirectAttributes);

        assertEquals("index", result);
        assertFalse(redirectAttributes.getFlashAttributes().containsKey("error"));
    }

    @Test
    @DisplayName("Should handle same day valid date")
    void shouldHandleSameDayValidDate() {
        LocalDate date = LocalDate.of(2023, 10, 4); // Wednesday

        String result = controller.calculate(date, date, model, redirectAttributes);

        assertEquals("index", result);
        assertFalse(redirectAttributes.getFlashAttributes().containsKey("error"));
    }

    @Test
    @DisplayName("Should return index page for root path")
    void shouldReturnIndexForRootPath() {
        String result = controller.index();
        assertEquals("index", result);
    }
}
