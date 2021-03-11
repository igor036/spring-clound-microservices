package com.linecode.linecodeframework.exception;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final HttpStatus status;
    private final String message;

    public RestException(HttpStatus status) {
        this.status = status;
        this.message = "";
    }

    public RestException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
