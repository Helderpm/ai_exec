package com.example.calculator.domain.usecase;

import com.example.calculator.domain.port.WorkingDayCalculator;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.stream.Stream;

/**
 * Domain service for calculating working days between two dates.
 * Excludes weekends and country-specific holidays.
 */
@Service
public class WorkingDayService implements WorkingDayCalculator {

    /**
     * Calculates working days between two dates (inclusive).
     * Excludes weekends (Saturday, Sunday) and country-specific holidays.
     *
     * @param start the start date
     * @param end the end date
     * @param countryCode ISO country code for holiday calculation
     * @return number of working days, or 0 if start is after end
     */
    @Override
    public long calculateWorkingDays(LocalDate start, LocalDate end, String countryCode) {
        if (start.isAfter(end)) return 0;
        
        // Map country code to HolidayCalendar
        String calendarCode = countryCode.toUpperCase();
        
        HolidayManager holidayManager = HolidayManager.getInstance(
                ManagerParameters.create(calendarCode)
        );
        
        return Stream.iterate(start, date -> date.plusDays(1))
                .limit(java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1)
                .filter(date -> !isWeekend(date))
                .filter(date -> !holidayManager.isHoliday(date, calendarCode))
                .count();
    }
    
    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
