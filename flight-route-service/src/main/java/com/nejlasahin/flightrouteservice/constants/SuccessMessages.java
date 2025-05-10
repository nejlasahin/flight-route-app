package com.nejlasahin.flightrouteservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SuccessMessages {
    public static final String LOCATION_CREATED = "Location has been created successfully";
    public static final String LOCATION_UPDATED = "Location has been updated successfully";
    public static final String LOCATION_DELETED_WITH_ID = "Location with ID %d has been deleted successfully";
    public static final String LOCATION_RETRIEVED_WITH_ID = "Location with ID %d retrieved successfully";
    public static final String LOCATIONS_RETRIEVED = "All locations retrieved successfully";

    public static final String TRANSPORTATIONS_RETRIEVED = "All transportations retrieved successfully";
    public static final String TRANSPORTATION_CREATED = "Transportation has been created successfully";
    public static final String TRANSPORTATION_UPDATED = "Transportation has been updated successfully";
    public static final String TRANSPORTATION_DELETED_WITH_ID = "Transportation with ID %d has been deleted successfully";
    public static final String TRANSPORTATION_RETRIEVED_WITH_ID = "Transportation with ID %d retrieved successfully";

    public static final String ROUTES_RETRIEVED_WITH_TRAVEL_DATE = "Routes between the specified locations for the date %s have been retrieved successfully";
}