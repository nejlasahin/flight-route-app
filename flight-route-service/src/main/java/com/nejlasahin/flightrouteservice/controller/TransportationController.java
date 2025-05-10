package com.nejlasahin.flightrouteservice.controller;

import com.nejlasahin.flightrouteservice.constants.EndpointPaths;
import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.dto.request.TransportationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;
import com.nejlasahin.flightrouteservice.general.RestResponse;
import com.nejlasahin.flightrouteservice.service.TransportationService;
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
@RequestMapping(EndpointPaths.TRANSPORTATIONS)
@Tag(name = "Transportation", description = "Operations related to transportation management")
public class TransportationController {
    private final TransportationService transportationService;

    @GetMapping
    @Operation(summary = "Get all transportations", description = "Retrieves a list of all transportation records.")
    public ResponseEntity<RestResponse<List<TransportationResponseDto>>> getAll() {
        List<TransportationResponseDto> responseDtoList = transportationService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.of(responseDtoList, SuccessMessages.TRANSPORTATIONS_RETRIEVED));
    }

    @PostMapping
    @Operation(summary = "Create a new transportation", description = "Saves a new transportation in the database.")
    public ResponseEntity<RestResponse<TransportationResponseDto>> save(@Valid @RequestBody TransportationRequestDto requestDto) {
        TransportationResponseDto responseDto = transportationService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(RestResponse.of(responseDto, SuccessMessages.TRANSPORTATION_CREATED));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a transportation", description = "Updates an existing transportation using its ID.")
    public ResponseEntity<RestResponse<TransportationResponseDto>> update(@Valid @RequestBody TransportationRequestDto requestDto, @PathVariable Long id) {
        TransportationResponseDto responseDto = transportationService.update(requestDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.of(responseDto, SuccessMessages.TRANSPORTATION_UPDATED));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transportation", description = "Deletes a transportation by its ID.")
    public ResponseEntity<RestResponse<String>> delete(@PathVariable Long id) {
        transportationService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.empty(String.format(SuccessMessages.TRANSPORTATION_DELETED_WITH_ID, id)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transportation by ID", description = "Retrieves a single transportation using its ID.")
    public ResponseEntity<RestResponse<TransportationResponseDto>> getById(@PathVariable Long id) {
        TransportationResponseDto responseDto = transportationService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.of(responseDto, String.format(SuccessMessages.TRANSPORTATION_RETRIEVED_WITH_ID, id)));
    }
}