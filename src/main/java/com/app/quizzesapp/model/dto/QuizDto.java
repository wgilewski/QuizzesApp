package com.app.quizzesapp.model.dto;

import com.app.quizzesapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class QuizDto
{
    private Long id;

    private boolean solved;
    private boolean competition;
    private LocalDate localDate;
    private Integer score;

    private LocalTime start;
    private LocalTime end;

    private Duration duration;

    private User user;
    private List<CountryDto> countries;
    private List<String> answers;
}
