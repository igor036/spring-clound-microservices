package com.linecode.linecodeframework.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;


public class UnprocessableEntityException extends RestException {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> errors;

    public UnprocessableEntityException(Map<String, String> errors) {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
        this.errors = errors;
    }

    public Map<String, String>  getErrors() {
        return errors;
    }
}