package com.nejlasahin.flightrouteservice.converter;

import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Transportation;
import com.nejlasahin.flightrouteservice.entity.TransportationOperatingDay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransportationConverter {
    public static TransportationResponseDto toTransportationResponseDto(Transportation transportation) {
        TransportationResponseDto responseDto = new TransportationResponseDto();
        responseDto.setId(transportation.getId());
        responseDto.setDestinationLocation(LocationConverter.toLocationResponseDto(transportation.getDestinationLocation()));
        responseDto.setOriginLocation(LocationConverter.toLocationResponseDto(transportation.getOriginLocation()));
        responseDto.setTransportationType(transportation.getTransportationType());
        if (transportation.getOperatingDays() != null) {
            List<Integer> dayList = transportation.getOperatingDays().stream()
                    .map(TransportationOperatingDay::getOperatingDay)
                    .collect(Collectors.toList());
            responseDto.setOperatingDays(dayList);
        }
        return responseDto;
    }

    public static List<TransportationResponseDto> toTransportationResponseDtoList(List<Transportation> transportations) {
        if (transportations == null || transportations.isEmpty()) {
            return new ArrayList<>();
        }
        return transportations.stream()
                .map(TransportationConverter::toTransportationResponseDto)
                .collect(Collectors.toList());
    }
}