package com.example.calculator.infrastructure.web;

import com.example.calculator.application.service.WorkingDayApplicationService;
import com.example.calculator.domain.model.WorkingDayResult;
import com.example.calculator.domain.model.WorkingDayError;
import com.example.calculator.domain.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Slf4j
@Controller
@Validated
@RequiredArgsConstructor
public class WorkingDayController {

    private final WorkingDayApplicationService applicationService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("countries", applicationService.getAllCountries());
        return "index";
    }

    @GetMapping("/calculate")
    public String calculate(
            @RequestParam("start") @NotNull(message = "Start date is required") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @NotNull(message = "End date is required") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam("country") String countryCode,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        log.info("Calculate request - start: {}, end: {}, country: {}", start, end, countryCode);
        
        ValidationResult validation = applicationService.validateRequest(start, end, countryCode);
        
        if (!validation.valid()) {
            log.error("Validation failed - errorCode: {}, message: {}", validation.error().getCode(), validation.error().getMessage());
            return getRedirectString(redirectAttributes, validation);
        }
        
        WorkingDayResult result = applicationService.calculateWorkingDays(start, end, countryCode, validation.country());
        log.info("Calculation successful - workingDays: {}, start: {}, end: {}, country: {}", 
                result.workingDays(), result.startDate(), result.endDate(), result.country().getCode());
        
        addResultToModel(model, result);
        return "index";
    }

    private static String getRedirectString(RedirectAttributes redirectAttributes, ValidationResult validation) {
        WorkingDayError error = validation.error();
        redirectAttributes.addFlashAttribute("error", error.getMessage());
        redirectAttributes.addFlashAttribute("errorCode", error.getCode());
        return "redirect:/";
    }

    private void addResultToModel(Model model, WorkingDayResult result) {
                model.addAttribute("result", result.workingDays());
                model.addAttribute("start", result.startDate());
                model.addAttribute("end", result.endDate());
                model.addAttribute("country", result.country().getName());
                model.addAttribute("countries", result.allCountries());
                model.addAttribute("selectedCountry", result.selectedCountryCode());
        }
}
