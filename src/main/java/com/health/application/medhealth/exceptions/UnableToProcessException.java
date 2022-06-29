package com.health.application.medhealth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableToProcessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnableToProcessException(String message) {
        super(message);
    }

    public UnableToProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
