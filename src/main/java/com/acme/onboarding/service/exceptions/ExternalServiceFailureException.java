package com.acme.onboarding.service.exceptions;

public class ExternalServiceFailureException extends Exception{
    public ExternalServiceFailureException(String message) {
        super(message);
    }

    public ExternalServiceFailureException(Exception e) {
        super(e);
    }
}
