package com.nejlasahin.flightrouteservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportationResponseDto {
    private Long id;

    @JsonProperty("origin_location")
    private LocationResponseDto originLocation;

    @JsonProperty("destination_location")
    private LocationResponseDto destinationLocation;

    @JsonProperty("transportation_type")
    private TransportationType transportationType;

    @JsonProperty("operating_days")
    private List<Integer> operatingDays;
}