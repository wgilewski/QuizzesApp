package com.app.quizzesapp.model.weather;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Weather
{
    private Coord coord;
    private MainInf main;
    private Wind wind;
}
