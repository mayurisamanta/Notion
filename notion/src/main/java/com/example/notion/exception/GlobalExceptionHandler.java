package com.example.notion.exception;

import com.example.notion.dto.ApiResp;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * handle UserException
     *
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
     * handle DocumentException
     *
     * @param documentException
     */
    @ExceptionHandler(DocumentException.class)
    public ApiResp<?> handleDocumentException(DocumentException documentException) {
        return ApiResp.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Document Related Error")
                .message(documentException.getMessage())
                .build();
    }

    /**
     * handle MethodArgumentNotValidException
     *
     * @param methodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResp<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errorMap = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errorMap.put(fieldName, defaultMessage);
        });
        return ApiResp.builder()
                .status(methodArgumentNotValidException.getStatusCode().value())
                .error("Validation Error")
                .message(methodArgumentNotValidException.getBody().getDetail())
                .data(errorMap)
                .build();
    }

    /**
     * handle Exception
     *
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
