package com.app.quizzesapp.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ExceptionInfo
{
    private final String message;
    private final LocalDateTime dateTime;

    public ExceptionInfo(String message) {
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}
