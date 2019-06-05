package com.app.quizzesapp.model.localization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Currency
{
    public String name;
    public String code;
    public String symbol;
    public String plural;
}
