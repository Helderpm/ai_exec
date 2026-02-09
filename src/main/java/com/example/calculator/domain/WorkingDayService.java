// src/main/java/com/example/calculator/domain/WorkingDayService.java
package com.example.calculator.domain;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class WorkingDayService {

/**
 * Calculates working days (Mon-Fri) between two dates, inclusive.
 */
public long calculateWorkingDays(LocalDate start, LocalDate end) {
    if (start.isAfter(end)) {
        return 0;
    }
    
    return Stream.iterate(start, date -> date.plusDays(1))
            .limit(ChronoUnit.DAYS.between(start, end) + 1)
            .filter(date -> !isWeekend(date))
            .count();
}

private boolean isWeekend(LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
}
}