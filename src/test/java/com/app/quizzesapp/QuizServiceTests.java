package com.app.quizzesapp;

import com.app.quizzesapp.model.Quiz;
import com.app.quizzesapp.model.User;
import com.app.quizzesapp.model.dto.QuizDto;
import com.app.quizzesapp.model.dto.UserDto;
import com.app.quizzesapp.repository.CountryRepository;
import com.app.quizzesapp.repository.QuizRepository;
import com.app.quizzesapp.repository.UserRepository;
import com.app.quizzesapp.service.QuizService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ExtendWith(SpringExtension.class)
public class QuizServiceTests
{
    @TestConfiguration
    public static class MyTestConfiguration
    {
        @MockBean
        private QuizRepository quizRepository;

        @MockBean
        private CountryRepository countryRepository;

        @MockBean
        private UserRepository userRepository;

        @Bean
        public QuizService quizService() {
            return new QuizService(quizRepository,countryRepository, userRepository);
        }
    }

    /*@Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserRepository userRepository;*/

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizService quizService;


    @Test
    public void test2()
    {
        //quizService.findAllQuizzesByUserId()
    }

    @Test void test3()
    {
        Mockito
                .when(quizRepository.findAll())
                .thenReturn(List.of(
                        Quiz.builder().user(User.builder().id(1L).build()).score(5).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(3L).build()).score(15).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(2L).build()).score(10).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build()));

        Optional<UserDto> userDto = quizService.findCompetitionWinner();

        Assertions.assertEquals(Long.valueOf(3),userDto.get().getId());

    }

    @Test void test4()
    {
        Mockito
                .when(quizRepository.findAll())
                .thenReturn(List.of(
                        Quiz.builder().user(User.builder().id(1L).build()).score(5).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(3L).build()).score(15).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(4L).build()).score(15).duration(Duration.ofSeconds(60)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(5L).build()).score(20).duration(Duration.ofSeconds(60)).localDate(LocalDate.now()).competition(false).build(),
                        Quiz.builder().user(User.builder().id(2L).build()).score(1).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build()));


        List<Long> size = quizService.scoreTable().stream().map(UserDto::getId).collect(Collectors.toList());
        Assertions.assertEquals(4,size.size());

        UserDto winner = quizService.scoreTable().stream().findFirst().get();
        Assertions.assertEquals(winner.getId(),size.get(0));
        Assertions.assertEquals(Long.valueOf(4),winner.getId());
    }

    @Test void test5()
    {
        Mockito
                .when(quizRepository.findAll())
                .thenReturn(List.of(
                        Quiz.builder().user(User.builder().id(1L).build()).score(5).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(3L).build()).score(15).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(4L).build()).score(15).duration(Duration.ofSeconds(60)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(5L).build()).score(20).duration(Duration.ofSeconds(60)).localDate(LocalDate.now()).competition(false).build(),
                        Quiz.builder().user(User.builder().id(2L).build()).score(1).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build()));

        List<Integer> integers1 = List.of(1,5,15,15,20);
        List<Integer> integers2 = List.of(20,15,15,5,1);
        List<Integer> desc = quizService.sortAllByScore("desc").stream().map(QuizDto::getScore).collect(Collectors.toList());
        List<Integer> asc = quizService.sortAllByScore("asc").stream().map(QuizDto::getScore).collect(Collectors.toList());


        Assertions.assertIterableEquals(integers1,asc);
        Assertions.assertIterableEquals(integers2,desc);

    }

    @Test void test6()
    {


       /* Mockito
                .when(quizRepository.findAll())
                .thenReturn(List.of(
                        Quiz.builder().user(User.builder().id(1L).build()).score(5).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(3L).build()).score(15).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(4L).build()).score(15).duration(Duration.ofSeconds(60)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(1L).build()).score(20).duration(Duration.ofSeconds(60)).localDate(LocalDate.now()).competition(false).build(),
                        Quiz.builder().user(User.builder().id(1L).build()).score(1).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build()));

        List<QuizDto> quizzes = quizService.sortUserQuizzesByScore("asc",1l);*/


    }

    @Test void test7()
    {
        /*Mockito
                .when(quizRepository.findAll())
                .thenReturn(List.of(
                        Quiz.builder().user(User.builder().id(1L).build()).score(5).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(3L).build()).score(15).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(4L).build()).score(15).duration(Duration.ofSeconds(60)).localDate(LocalDate.now()).competition(true).build(),
                        Quiz.builder().user(User.builder().id(1L).build()).score(20).duration(Duration.ofSeconds(60)).localDate(LocalDate.now()).competition(false).build(),
                        Quiz.builder().user(User.builder().id(1L).build()).score(1).duration(Duration.ofSeconds(123)).localDate(LocalDate.now()).competition(true).build()));


        Optional<QuizDto> q = quizService.getOneQuiz(1L);*/
    }
    @Test void test8()
    {
        Mockito
                .when(quizRepository.findAll())
                .thenReturn(List.of(
                        Quiz.builder().user(User.builder().id(1L).build()).score(5).solved(true).build(),
                        Quiz.builder().user(User.builder().id(1L).build()).score(15).solved(true).build(),
                        Quiz.builder().user(User.builder().id(1L).build()).score(15).solved(true).build(),
                        Quiz.builder().user(User.builder().id(1L).build()).score(20).solved(false).build(),
                        Quiz.builder().user(User.builder().id(1L).build()).score(1).solved(false).build()));


        List<QuizDto> quizzes1 = quizService.findAllSolvedOrUnsolvedQuizzes(true,1L);
        Assertions.assertEquals(3,quizzes1.size());

        List<QuizDto> quizzes2 = quizService.findAllSolvedOrUnsolvedQuizzes(false,1L);
        Assertions.assertEquals(2,quizzes2.size());
    }


}
