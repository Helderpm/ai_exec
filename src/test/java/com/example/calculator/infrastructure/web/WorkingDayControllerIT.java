package com.example.calculator.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class WorkingDayControllerIT {

@Autowired
private MockMvc mockMvc;

    @Test
    void shouldReturnCalculationResultToView() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("start", "2023-10-02")
                        .param("end", "2023-10-04"))
                .andExpect(status().isOk())
                // 1. Verify we hit the right file
                .andExpect(view().name("index"))
                // 2. Verify the MATH is correct in the model (Most Important)
                .andExpect(model().attribute("result", 3L));
    }
}