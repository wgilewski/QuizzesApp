package com.app.quizzesapp.model.country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Country
{
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String alpha3Code;
    private String capital;
    private String region;
    private String subregion;
    private Integer population;
    private Double area;
    private String nativeName;

    @ElementCollection
    private List<String>  altSpellings;
    @ElementCollection
    private List<String> timezones;
    @ElementCollection
    private List<String>  borders;
    @ElementCollection
    private List<String>  currencies;
    @ElementCollection
    private List<String>  languages;

}
