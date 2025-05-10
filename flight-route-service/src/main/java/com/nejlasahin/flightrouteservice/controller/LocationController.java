package com.nejlasahin.flightrouteservice.controller;

import com.nejlasahin.flightrouteservice.constants.EndpointPaths;
import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.dto.request.LocationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.general.RestResponse;
import com.nejlasahin.flightrouteservice.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointPaths.LOCATIONS)
@Tag(name = "Location", description = "Operations related to location management")
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    @Operation(summary = "Get all locations", description = "Retrieves a list of all location records.")
    public ResponseEntity<RestResponse<List<LocationResponseDto>>> getAll() {
        List<LocationResponseDto> responseDtoList = locationService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.of(responseDtoList, SuccessMessages.LOCATIONS_RETRIEVED));
    }

    @PostMapping
    @Operation(summary = "Create a new location", description = "Saves a new location in the database.")
    public ResponseEntity<RestResponse<LocationResponseDto>> save(@Valid @RequestBody LocationRequestDto requestDto) {
        LocationResponseDto responseDto = locationService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(RestResponse.of(responseDto, SuccessMessages.LOCATION_CREATED));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a location", description = "Updates an existing location using its ID.")
    public ResponseEntity<RestResponse<LocationResponseDto>> update(@Valid @RequestBody LocationRequestDto requestDto, @PathVariable Long id) {
        LocationResponseDto responseDto = locationService.update(requestDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.of(responseDto, SuccessMessages.LOCATION_UPDATED));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a location", description = "Deletes a location by its ID.")
    public ResponseEntity<RestResponse<String>> delete(@PathVariable Long id) {
        locationService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.empty(String.format(SuccessMessages.LOCATION_DELETED_WITH_ID, id)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get location by ID", description = "Retrieves a single location using its ID.")
    public ResponseEntity<RestResponse<LocationResponseDto>> getById(@PathVariable Long id) {
        LocationResponseDto responseDto = locationService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.of(responseDto, String.format(SuccessMessages.LOCATION_RETRIEVED_WITH_ID, id)));
    }
}