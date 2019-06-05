package com.app.quizzesapp.model.country.informations;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Details
{
    private Double distance;
    private List<Stop> stops = new ArrayList<>();
    private Travel travel;
}

