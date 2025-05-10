package com.nejlasahin.flightrouteservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationMessages {
    public static final String NAME_NOT_BLANK = "Name cannot be blank";
    public static final String NAME_SIZE = "Name can have at most 100 characters";

    public static final String COUNTRY_NOT_BLANK = "Country cannot be blank";
    public static final String COUNTRY_SIZE = "Country can have at most 100 characters";

    public static final String CITY_NOT_BLANK = "City cannot be blank";
    public static final String CITY_SIZE = "City can have at most 100 characters";

    public static final String LOCATION_CODE_NOT_BLANK = "Location code cannot be blank";
    public static final String LOCATION_CODE_SIZE = "Location code must be 3-50 characters";

    public static final String ORIGIN_LOCATION_ID_NOT_NULL = "Origin Location ID cannot be null";
    public static final String ORIGIN_LOCATION_ID_POSITIVE = "Origin Location ID must be a positive number";

    public static final String DESTINATION_LOCATION_ID_NOT_NULL = "Destination Location ID cannot be null";
    public static final String DESTINATION_LOCATION_ID_POSITIVE = "Destination Location ID must be a positive number";

    public static final String TRANSPORTATION_TYPE_NOT_NULL = "Transportation Type cannot be null";

    public static final String OPERATING_DAYS_NOT_EMPTY = "Operating Days cannot be empty";
    public static final String OPERATING_DAYS_MIN = "There must be at least one operating day";

    public static final String INVALID_OPERATING_DAYS = "Operating days must be between 1 and 7";
    public static final String INVALID_TRANSPORTATION_TYPE = "Invalid transportation type";
}
