package com.example.calculator.domain.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkingDayServiceTest {

    private final WorkingDayService service = new WorkingDayService();
    
    @Test
    @DisplayName("Should return 4 days for a full Monday-Friday week with German holiday")
    void testStandardWeek() {
        LocalDate start = LocalDate.of(2023, 10, 2); // Monday
        LocalDate end = LocalDate.of(2023, 10, 6);   // Friday
        
        long result = service.calculateWorkingDays(start, end, "DE");
        
        assertEquals(4, result, "A work week with German Unity Day should be 4 days");
    }

    @Test
    @DisplayName("Should exclude weekends when dates span across Saturday/Sunday")
    void testExcludeWeekends() {
        LocalDate start = LocalDate.of(2023, 10, 6);  // Friday
        LocalDate end = LocalDate.of(2023, 10, 10); // Tuesday
        
        // Days: Fri(1), Sat(X), Sun(X), Mon(2), Tue(3)
        long result = service.calculateWorkingDays(start, end, "DE");
        
        assertEquals(3, result);
    }

    @Test
    @DisplayName("Should return 1 if start and end are the same working day")
    void testSameDay() {
        LocalDate day = LocalDate.of(2023, 10, 4); // Wednesday
        assertEquals(1, service.calculateWorkingDays(day, day, "DE"));
    }

    @Test
    @DisplayName("Should return 0 if start and end are the same weekend day")
    void testSameDayWeekend() {
        LocalDate day = LocalDate.of(2023, 10, 7); // Saturday
        assertEquals(0, service.calculateWorkingDays(day, day, "DE"));
    }

    @Test
    @DisplayName("Should exclude German holidays from working days calculation")
    void testExcludeGermanHolidays() {
        // Test around German Unity Day (Oct 3, 2023 - Tuesday)
        LocalDate start = LocalDate.of(2023, 10, 2); // Monday
        LocalDate end = LocalDate.of(2023, 10, 6);   // Friday
        
        long result = service.calculateWorkingDays(start, end, "DE");
        
        // Should be 4 days (Mon, Thu, Fri) - Tuesday is holiday (German Unity Day)
        assertEquals(4, result, "Should exclude German Unity Day on Oct 3");
    }

    @Test
    @DisplayName("Should exclude French holidays from working days calculation")
    void testExcludeFrenchHolidays() {
        // Test around Bastille Day (July 14, 2023 - Friday)
        LocalDate start = LocalDate.of(2023, 7, 10); // Monday
        LocalDate end = LocalDate.of(2023, 7, 14);   // Friday
        
        long result = service.calculateWorkingDays(start, end, "FR");
        
        // Should be 4 days (Mon, Tue, Wed, Thu) - Friday is holiday (Bastille Day)
        assertEquals(4, result, "Should exclude Bastille Day on July 14");
    }

    @Test
    @DisplayName("Should handle different countries with different holidays")
    void testDifferentCountryHolidays() {
        LocalDate start = LocalDate.of(2023, 10, 2); // Monday
        LocalDate end = LocalDate.of(2023, 10, 6);   // Friday
        
        long germanResult = service.calculateWorkingDays(start, end, "DE");
        long frenchResult = service.calculateWorkingDays(start, end, "FR");
        
        // Germany has holiday on Oct 3 (German Unity Day), France doesn't
        assertEquals(4, germanResult, "Germany should have 4 days (holiday on Oct 3)");
        assertEquals(5, frenchResult, "France should have 5 days (no holiday on Oct 3)");
    }

    @Test
    @DisplayName("Should return 0 when start date is after end date")
    void testStartAfterEnd() {
        LocalDate start = LocalDate.of(2023, 10, 10);
        LocalDate end = LocalDate.of(2023, 10, 5);
        
        assertEquals(0, service.calculateWorkingDays(start, end, "DE"));
    }
}
