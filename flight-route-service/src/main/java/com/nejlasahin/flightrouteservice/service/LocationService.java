package com.nejlasahin.flightrouteservice.service;

import com.nejlasahin.flightrouteservice.dto.request.LocationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Location;

import java.util.List;

public interface LocationService {

    List<LocationResponseDto> getAll();

    LocationResponseDto save(LocationRequestDto requestDto);

    LocationResponseDto update(LocationRequestDto requestDto, Long id);

    void deleteById(Long id);

    LocationResponseDto getById(Long id);

    Location getEntityById(Long id);

    String getLocationCodeById(Long id);

    void validateExistsById(Long id);
}

