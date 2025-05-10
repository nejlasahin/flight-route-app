package com.nejlasahin.flightrouteservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessages {
    public static final String LOCATION_CODE_ALREADY_EXISTS = "The location code %s already exists";
    public static final String LOCATION_NOT_FOUND = "Location not found with id %s";
    public static final String SAME_LOCATION_EXCEPTION = "Origin and destination locations cannot be the same. Please select different locations.";

    public static final String TRANSPORTATION_ALREADY_EXISTS = "The transportation from %s to %s with type %s already exists";
    public static final String TRANSPORTATION_NOT_FOUND = "Transportation not found with id %s";
    public static final String TRANSPORTATION_PARAMETERS_MISSING = "Origin, destination, and travel date must all be provided";
    public static final String TRAVEL_DATE_IN_PAST = "Travel date cannot be in the past";
}