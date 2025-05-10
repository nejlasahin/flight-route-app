package com.nejlasahin.flightrouteservice.validator;

import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class TransportationTypeValidator implements ConstraintValidator<ValidTransportationType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return Arrays.stream(TransportationType.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(value));
    }
}