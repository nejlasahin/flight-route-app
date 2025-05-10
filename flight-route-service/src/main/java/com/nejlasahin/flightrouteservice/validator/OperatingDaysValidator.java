package com.nejlasahin.flightrouteservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OperatingDaysValidator implements ConstraintValidator<ValidOperatingDays, List<Integer>> {
    @Override
    public boolean isValid(List<Integer> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        Set<Integer> uniqueDays = new HashSet<>();
        for (Integer day : value) {
            if (day < 1 || day > 7 || !uniqueDays.add(day)) {
                return false;
            }
        }
        return true;
    }
}