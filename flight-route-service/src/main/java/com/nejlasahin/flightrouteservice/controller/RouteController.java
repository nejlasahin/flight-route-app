package com.nejlasahin.flightrouteservice.controller;

import com.nejlasahin.flightrouteservice.constants.EndpointPaths;
import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.dto.response.RouteResponseDto;
import com.nejlasahin.flightrouteservice.general.RestResponse;
import com.nejlasahin.flightrouteservice.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointPaths.ROUTES)
@Tag(name = "Route", description = "Route calculation operations")
public class RouteController {
    private final RouteService routeService;

    @GetMapping
    @Operation(summary = "Get routes by date", description = "Fetches all valid routes between locations based on the specified travel date")
    public ResponseEntity<RestResponse<List<RouteResponseDto>>> getAllByDate(
            @RequestParam(name = "origin_location_id") Long originLocationId,
            @RequestParam(name = "destination_location_id") Long destinationLocationId,
            @RequestParam(name = "travel_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate travelDate
    ) {
        List<RouteResponseDto> responseDtoList = routeService.getAllByDate(originLocationId, destinationLocationId, travelDate);
        String message = String.format(SuccessMessages.ROUTES_RETRIEVED_WITH_TRAVEL_DATE, travelDate);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.of(responseDtoList, message));
    }
}