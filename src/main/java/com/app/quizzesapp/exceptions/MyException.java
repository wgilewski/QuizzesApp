package com.app.quizzesapp.exceptions;

public class MyException extends RuntimeException {
    private String exceptionMessage;

    public MyException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}