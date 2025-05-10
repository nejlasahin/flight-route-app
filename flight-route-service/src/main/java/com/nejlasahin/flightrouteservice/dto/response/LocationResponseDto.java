package com.nejlasahin.flightrouteservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDto {
    private Long id;

    private String name;

    private String country;

    private String city;

    @JsonProperty("location_code")
    private String locationCode;
}