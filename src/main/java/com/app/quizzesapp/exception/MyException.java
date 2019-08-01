package com.app.quizzesapp.exception;


import lombok.Getter;

@Getter
public class MyException extends RuntimeException{
    private ExceptionInfo exceptionInfo;

    public MyException(String exceptionMessage) {
        this.exceptionInfo = new ExceptionInfo(exceptionMessage);
    }

    @Override
    public String getMessage() {
        return exceptionInfo.getMessage();
    }
}
