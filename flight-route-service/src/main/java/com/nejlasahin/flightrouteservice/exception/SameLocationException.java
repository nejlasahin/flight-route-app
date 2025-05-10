package com.nejlasahin.flightrouteservice.exception;

public class SameLocationException extends RuntimeException {
    public SameLocationException(String message) {
        super(message);
    }
}