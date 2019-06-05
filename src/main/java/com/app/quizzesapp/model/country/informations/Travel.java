package com.app.quizzesapp.model.country.informations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Travel
{
    private Destination destination;
    private Integer airports;
}
