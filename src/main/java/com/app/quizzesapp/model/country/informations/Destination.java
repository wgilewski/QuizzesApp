package com.app.quizzesapp.model.country.informations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destination
{
    private List<Airport> airports;
}
