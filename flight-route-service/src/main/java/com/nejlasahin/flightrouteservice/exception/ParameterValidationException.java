package com.nejlasahin.flightrouteservice.exception;

public class ParameterValidationException extends RuntimeException {
    public ParameterValidationException(String message) {
        super(message);
    }
}