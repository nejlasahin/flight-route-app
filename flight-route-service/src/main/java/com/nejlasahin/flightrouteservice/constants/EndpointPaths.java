package com.nejlasahin.flightrouteservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointPaths {
    public static final String BASE_URL = "/api/v1";
    public static final String LOCATIONS = BASE_URL + "/locations";
    public static final String TRANSPORTATIONS = BASE_URL + "/transportations";
    public static final String ROUTES = BASE_URL + "/routes";
}