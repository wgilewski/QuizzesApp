package com.app.quizzesapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CountryDto
{
    private Long id;
    private String name;
    private String alpha3Code;
    private String capital;
    private String region;
    private String subRegion;
    private Integer population;
    private Double area;
    private String nativeName;

    private List<String> altSpellings;
    private List<String> timezones;
    private List<String>  borders;
    private List<String>  currencies;
    private List<String>  languages;
}
