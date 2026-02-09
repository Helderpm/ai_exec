
package com.example.calculator.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkingDayServiceTest {

    private final WorkingDayService service = new WorkingDayService();
    
    @Test
    @DisplayName("Should return 5 days for a full Monday-Friday week")
    void testStandardWeek() {
        LocalDate start = LocalDate.of(2023, 10, 2); // Monday
        LocalDate end = LocalDate.of(2023, 10, 6);   // Friday
        
        long result = service.calculateWorkingDays(start, end);
        
        assertEquals(5, result, "A full work week should be 5 days");
    }

    @Test
    @DisplayName("Should exclude weekends when dates span across Saturday/Sunday")
    void testExcludeWeekends() {
        LocalDate start = LocalDate.of(2023, 10, 6);  // Friday
        LocalDate end = LocalDate.of(2023, 10, 10); // Tuesday
        
        // Days: Fri(1), Sat(X), Sun(X), Mon(2), Tue(3)
        long result = service.calculateWorkingDays(start, end);
        
        assertEquals(3, result);
    }

    @Test
    @DisplayName("Should return 1 if start and end are the same working day")
    void testSameDay() {
        LocalDate day = LocalDate.of(2023, 10, 4); // Wednesday
        assertEquals(1, service.calculateWorkingDays(day, day));
    }

    @Test
    @DisplayName("Should return 0 if start and end are the same weekend day")
    void testSameDayWeekend() {
        LocalDate day = LocalDate.of(2023, 10, 7); // Saturday
        assertEquals(0, service.calculateWorkingDays(day, day));
    }
}