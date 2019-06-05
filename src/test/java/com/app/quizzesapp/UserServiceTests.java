package com.app.quizzesapp;

import com.app.quizzesapp.model.Quiz;
import com.app.quizzesapp.model.User;
import com.app.quizzesapp.model.dto.UserDto;
import com.app.quizzesapp.repository.UserRepository;
import com.app.quizzesapp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
public class UserServiceTests
{
    @TestConfiguration
    public static class MyTestConfiguration
    {
        @MockBean
        private UserRepository userRepository;

        @Bean
        public UserService userService()
        {
            return new UserService(userRepository);
        }

    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void test1()
    {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(User.builder().id(1l).name("A").surname("A").build()));

        List<UserDto> users = userService.findAll();
        Assertions.assertEquals(1,users.size());
    }

    @Test
    public void test2()
    {
        Mockito
                .when(userRepository.findById(1l))
                .thenReturn(Optional.of(User.builder()
                        .id(1l)
                        .name("A")
                        .surname("A")
                        .quizzes(new ArrayList<>(Arrays.asList(Quiz.builder().build(),Quiz.builder().build()))).build()));

        Optional<UserDto> userDto = userService.findOne(1l);
        Assertions.assertEquals("A",userDto.get().getName());
        Assertions.assertEquals("A",userDto.get().getSurname());
        Assertions.assertEquals(2,userDto.get().getQuizzes().size());
    }




}
