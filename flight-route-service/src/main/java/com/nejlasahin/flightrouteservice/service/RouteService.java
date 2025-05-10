package com.nejlasahin.flightrouteservice.service;

import com.nejlasahin.flightrouteservice.dto.response.RouteResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface RouteService {

    List<RouteResponseDto> getAllByDate(Long originLocationId, Long destinationLocationId, LocalDate travelDate);
}
