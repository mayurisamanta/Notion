package com.example.notion.exception;

import com.example.notion.dto.ApiResp;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * handleUserException
     * @param userException
     */
    @ExceptionHandler(UserException.class)
    public ApiResp<?> handleUserException(UserException userException) {
        return ApiResp.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("User Related Error")
                .message(userException.getMessage())
                .build();
    }

    /**
     * handleException
     * @param exception
     */
    @ExceptionHandler(Exception.class)
    public ApiResp<?> handleException(Exception exception) {
        return ApiResp.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(exception.getMessage())
                .build();
    }
}
