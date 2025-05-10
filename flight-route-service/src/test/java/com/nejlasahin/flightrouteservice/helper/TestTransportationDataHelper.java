package com.nejlasahin.flightrouteservice.helper;

import com.nejlasahin.flightrouteservice.dto.request.TransportationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.entity.Transportation;
import com.nejlasahin.flightrouteservice.entity.TransportationOperatingDay;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.repository.TransportationRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestTransportationDataHelper {

    public static TransportationRequestDto createTransportationRequestDto(long originLocationId, long destinationLocationId, TransportationType transportationType) {
        TransportationRequestDto requestDto = new TransportationRequestDto();
        requestDto.setOriginLocationId(originLocationId);
        requestDto.setDestinationLocationId(destinationLocationId);
        requestDto.setTransportationType(transportationType.toString());
        requestDto.setOperatingDays(List.of(1, 2, 3, 4, 5));
        return requestDto;
    }

    public static TransportationResponseDto createTransportationResponseDto(TransportationType transportationType) {
        TransportationResponseDto responseDto = new TransportationResponseDto();
        responseDto.setId(ThreadLocalRandom.current().nextLong());
        responseDto.setOriginLocation(TestLocationDataHelper.createLocationResponseDto());
        responseDto.setDestinationLocation(TestLocationDataHelper.createLocationResponseDto());
        responseDto.setTransportationType(transportationType);
        responseDto.setOperatingDays(List.of(1, 2, 3, 4, 5, 6, 7));
        return responseDto;
    }

    public static TransportationResponseDto createTransportationResponseDto(TransportationType transportationType, LocationResponseDto originLocation, LocationResponseDto destinationLocation) {
        TransportationResponseDto responseDto = new TransportationResponseDto();
        responseDto.setId(ThreadLocalRandom.current().nextLong());
        responseDto.setOriginLocation(originLocation);
        responseDto.setDestinationLocation(destinationLocation);
        responseDto.setTransportationType(transportationType);
        responseDto.setOperatingDays(List.of(1, 2, 3, 4, 5, 6, 7));
        return responseDto;
    }

    public static Transportation createTransportationEntity(Location originLocation, Location destinationLocation, TransportationType type, int days) {
        Transportation transportation = new Transportation();
        List<TransportationOperatingDay> transportationOperatingDays = createTransportationOperatingDays(days, transportation);
        transportation.getOperatingDays().clear();
        transportation.setOperatingDays(transportationOperatingDays);
        transportation.setDestinationLocation(destinationLocation);
        transportation.setOriginLocation(originLocation);
        transportation.setTransportationType(type);
        return transportation;
    }

    public static Transportation createTransportationEntityWithId(Location originLocation, Location destinationLocation, TransportationType type, int days) {
        Transportation transportation = new Transportation();
        transportation.setId(ThreadLocalRandom.current().nextLong());
        List<TransportationOperatingDay> transportationOperatingDays = createTransportationOperatingDays(days, transportation);
        transportation.getOperatingDays().clear();
        transportation.setOperatingDays(transportationOperatingDays);
        transportation.setDestinationLocation(destinationLocation);
        transportation.setOriginLocation(originLocation);
        transportation.setTransportationType(type);
        return transportation;
    }

    public static Transportation createAndSaveTransportation(Location originLocation, Location destinationLocation, TransportationType type, int days, TransportationRepository transportationRepository) {
        Transportation transportation = createTransportationEntity(originLocation, destinationLocation, type, days);
        return transportationRepository.save(transportation);
    }

    private static List<TransportationOperatingDay> createTransportationOperatingDays(int days, Transportation transportation) {
        return Stream.iterate(1, day -> day + 1)
                .limit(days)
                .map(day -> new TransportationOperatingDay(day, transportation))
                .collect(Collectors.toList());
    }
}
