package com.app.quizzesapp.model.country.informations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Airport
{
    private String home;
    private String iata;
    private String name;
}
