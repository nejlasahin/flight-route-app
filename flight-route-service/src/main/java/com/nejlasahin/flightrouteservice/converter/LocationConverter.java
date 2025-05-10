package com.nejlasahin.flightrouteservice.converter;

import com.nejlasahin.flightrouteservice.dto.request.LocationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocationConverter {
    public static Location toLocation(LocationRequestDto requestDto) {
        Location location = new Location();
        setLocationFields(location, requestDto);
        return location;
    }

    public static Location toLocation(Location location, LocationRequestDto requestDto) {
        setLocationFields(location, requestDto);
        return location;
    }

    public static LocationResponseDto toLocationResponseDto(Location location) {
        LocationResponseDto responseDto = new LocationResponseDto();
        responseDto.setId(location.getId());
        responseDto.setName(location.getName());
        responseDto.setCity(location.getCity());
        responseDto.setCountry(location.getCountry());
        responseDto.setLocationCode(location.getLocationCode());
        return responseDto;
    }

    public static List<LocationResponseDto> toLocationResponseDtoList(List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            return new ArrayList<>();
        }
        return locations.stream()
                .map(LocationConverter::toLocationResponseDto)
                .collect(Collectors.toList());
    }

    private static void setLocationFields(Location location, LocationRequestDto requestDto) {
        location.setName(requestDto.getName());
        location.setCity(requestDto.getCity());
        location.setCountry(requestDto.getCountry());
        location.setLocationCode(requestDto.getLocationCode());
    }
}