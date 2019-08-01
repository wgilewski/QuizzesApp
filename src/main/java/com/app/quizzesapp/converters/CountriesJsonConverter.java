package com.app.quizzesapp.converters;

import com.app.quizzesapp.model.Countries;

public class CountriesJsonConverter extends JsonConverter<Countries>
{
    public CountriesJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
