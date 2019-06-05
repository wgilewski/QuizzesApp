package com.app.quizzesapp.model.dto;

import com.app.quizzesapp.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto
{
    private Long id;
    private String name;
    private String surname;

    private List<Quiz> quizzes;
}
