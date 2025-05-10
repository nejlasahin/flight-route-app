package com.nejlasahin.flightrouteservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponseDto {
    @JsonProperty("transportations")
    private List<TransportationResponseDto> transportations;
}