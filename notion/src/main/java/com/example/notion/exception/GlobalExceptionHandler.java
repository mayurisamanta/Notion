package com.example.notion.exception;

import com.example.notion.dto.ApiResp;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResp<?> handleException(Exception e) {
        return ApiResp.<String>builder()
                .status(500)
                .message(e.getMessage())
                .build();
    }
}
