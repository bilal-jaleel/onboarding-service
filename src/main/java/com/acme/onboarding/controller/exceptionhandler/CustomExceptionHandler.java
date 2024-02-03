package com.acme.onboarding.controller.exceptionhandler;

import com.acme.onboarding.controller.response.GenericResponse;
import com.acme.onboarding.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericResponse<String>> handleValidationException(ValidationException ex) {
        GenericResponse<String> response = GenericResponse.<String>builder().data(ex.getMessage()).status(false).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
