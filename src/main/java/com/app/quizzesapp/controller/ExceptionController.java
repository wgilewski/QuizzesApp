package com.app.quizzesapp.controller;

import com.app.quizzesapp.exception.Info;
import com.app.quizzesapp.exception.MyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController
{
    @ExceptionHandler({MyException.class})
    public Info myExceptionHandler(MyException e) {
        return Info.builder().exception(e.getExceptionInfo()).data(null).build();
    }
}
