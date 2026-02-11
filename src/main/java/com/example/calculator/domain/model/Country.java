package com.example.calculator.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("name")
    private String name;
}
