// src/main/java/com/example/calculator/infrastructure/web/WorkingDayController.java
package com.example.calculator.infrastructure.web;

import com.example.calculator.domain.WorkingDayService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Controller
@Validated
public class WorkingDayController {

    private final WorkingDayService workingDayService;

    @Autowired
    public WorkingDayController(WorkingDayService workingDayService) {
        this.workingDayService = workingDayService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/calculate")
    public String calculate(
            @RequestParam("start") @NotNull(message = "Start date is required") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @NotNull(message = "End date is required") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        String validationError = validateDateRange(start, end);
        if (validationError != null) {
            redirectAttributes.addFlashAttribute("error", validationError);
            return "redirect:/";
        }
        
        long result = workingDayService.calculateWorkingDays(start, end);
        addResultToModel(model, result, start, end);
        return "index";
    }
    
    private String validateDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return "Start date cannot be after end date";
        }
        
        if (start.isBefore(LocalDate.of(1900, 1, 1)) || end.isAfter(LocalDate.of(2100, 12, 31))) {
            return "Date range must be between 1900-01-01 and 2100-12-31";
        }
        
        return null;
    }
    
    private void addResultToModel(Model model, long result, LocalDate start, LocalDate end) {
        model.addAttribute("result", result);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
    }
}
