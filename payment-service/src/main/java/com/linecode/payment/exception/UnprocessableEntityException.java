package com.linecode.payment.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UnprocessableEntityException extends RestException {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> errors;

    public UnprocessableEntityException(Map<String, String> errors) {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
        this.errors = errors;
    }

}