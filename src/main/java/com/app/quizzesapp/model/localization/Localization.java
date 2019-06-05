package com.app.quizzesapp.model.localization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Localization
{
    public String ip;
    public String city ;
    public String region ;
    public String regionCode ;
    public String countryName ;
    public String countryCode ;
    public String continentName ;
    public String continentCode ;
    public Double latitude ;
    public Double longitude ;
    public String asn ;
    public String organisation ;
    public String postal ;
    public String callingCode ;
    public String flag ;
    public Currency currency ;
    public String count ;
}
