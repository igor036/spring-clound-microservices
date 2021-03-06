package com.linecode.linecodeframework.config;

import com.linecode.linecodeframework.exception.RestException;
import com.linecode.linecodeframework.exception.UnprocessableEntityException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public abstract class LinecodeRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected internal error.";

    @ExceptionHandler(UnprocessableEntityException.class)
    protected ResponseEntity<Object> unprocessableEntityExceptionhandler(UnprocessableEntityException ex) {
        return ResponseEntity
            .unprocessableEntity()
            .body(ex.getErrors());
    }

    @ExceptionHandler(RestException.class)
    protected ResponseEntity<String> restExceptionHandler(RestException ex) {
        return ResponseEntity
            .status(ex.getStatus())
            .body(ex.getMessage());
    }   

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<String> unexpectedErrorHandler(RuntimeException ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(UNEXPECTED_ERROR_MESSAGE);
    }
}
