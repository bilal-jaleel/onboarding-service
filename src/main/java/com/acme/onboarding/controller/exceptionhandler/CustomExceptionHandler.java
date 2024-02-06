package com.acme.onboarding.controller.exceptionhandler;

import com.acme.onboarding.controller.response.GenericResponse;
import com.acme.onboarding.service.exceptions.ExternalServiceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    // Any other exceptions will be caught by global spring exception handler

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericResponse<String>> handleValidationException(ValidationException ex) {
        GenericResponse<String> response = GenericResponse.<String>builder().data(ex.getMessage()).status(false).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<String>> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldErrors().getFirst();

        GenericResponse<String> response = GenericResponse.<String>builder().data(fieldError.getField() + " " + fieldError.getDefaultMessage()).status(false).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GenericResponse<String>> handleRequestParamNotValidException(MissingServletRequestParameterException ex){
        GenericResponse<String> response = GenericResponse.<String>builder().data(ex.getMessage()).status(false).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalServiceFailureException.class)
    public ResponseEntity<GenericResponse<String>> handleExternalServiceFailureException(MissingServletRequestParameterException ex){
        GenericResponse<String> response = GenericResponse.<String>builder().data(ex.getMessage()).status(false).build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
