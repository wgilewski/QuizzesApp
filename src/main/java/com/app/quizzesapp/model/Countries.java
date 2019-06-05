package com.app.quizzesapp.model;

import com.app.quizzesapp.model.country.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Countries
{
    private List<Country> countries;
}
