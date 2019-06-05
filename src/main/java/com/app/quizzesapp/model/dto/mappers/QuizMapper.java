package com.app.quizzesapp.model.dto.mappers;

import com.app.quizzesapp.model.Quiz;
import com.app.quizzesapp.model.dto.QuizDto;
import org.mapstruct.Mapper;

@Mapper
public interface QuizMapper
{
    Quiz quizDtoToQuiz(QuizDto quizDto);
    QuizDto quizToQuizDto(Quiz quiz);

}
