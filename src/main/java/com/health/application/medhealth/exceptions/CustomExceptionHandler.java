package com.health.application.medhealth.exceptions;

import com.health.application.medhealth.dto.ExceptionFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnableToProcessException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UnableToProcessException ex, WebRequest request) {
        ExceptionFormat format = new ExceptionFormat(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(format, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleAuthException(AuthenticationException ex, WebRequest request) {
        ExceptionFormat format = new ExceptionFormat(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(format, HttpStatus.UNAUTHORIZED);
    }

}
