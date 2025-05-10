package com.nejlasahin.flightrouteservice.helper;

import com.nejlasahin.flightrouteservice.dto.request.LocationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.repository.LocationRepository;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class TestLocationDataHelper {

    public static LocationRequestDto createLocationRequestDto() {
        LocationRequestDto requestDto = new LocationRequestDto();
        requestDto.setLocationCode(generateRandomString());
        requestDto.setName(generateRandomString());
        requestDto.setCity(generateRandomString());
        requestDto.setCountry(generateRandomString());
        return requestDto;
    }

    public static LocationRequestDto createLocationRequestDto(Location location) {
        LocationRequestDto requestDto = new LocationRequestDto();
        requestDto.setLocationCode(location.getLocationCode());
        requestDto.setName(location.getName());
        requestDto.setCity(location.getCity());
        requestDto.setCountry(location.getCountry());
        return requestDto;
    }

    public static LocationResponseDto createLocationResponseDto(Location location) {
        LocationResponseDto responseDto = new LocationResponseDto();
        responseDto.setLocationCode(location.getLocationCode());
        responseDto.setName(location.getName());
        responseDto.setCity(location.getCity());
        responseDto.setCountry(location.getCountry());
        return responseDto;
    }

    public static LocationResponseDto createLocationResponseDto() {
        LocationResponseDto responseDto = new LocationResponseDto();
        responseDto.setId(ThreadLocalRandom.current().nextLong());
        responseDto.setLocationCode(generateRandomString());
        responseDto.setName(generateRandomString());
        responseDto.setCity(generateRandomString());
        responseDto.setCountry(generateRandomString());
        return responseDto;
    }

    public static Location createAndSaveLocation(LocationRepository locationRepository) {
        Location location = createLocationEntity();
        return locationRepository.save(location);
    }

    public static void createAndSaveLocationWhenLocationCode(LocationRepository locationRepository, String locationCode) {
        Location location = createLocationEntity();
        location.setLocationCode(locationCode);
        locationRepository.save(location);
    }

    public static Location createLocationEntity() {
        Location location = new Location();
        location.setLocationCode(generateRandomString());
        location.setName(generateRandomString());
        location.setCity(generateRandomString());
        location.setCountry(generateRandomString());
        return location;
    }

    public static Location createLocationEntityWithId() {
        Location location = new Location();
        location.setId(ThreadLocalRandom.current().nextLong());
        location.setLocationCode(generateRandomString());
        location.setName(generateRandomString());
        location.setCity(generateRandomString());
        location.setCountry(generateRandomString());
        return location;
    }

    private static String generateRandomString() {
        return new Random()
                .ints(5, 'A', 'z' + 1)
                .filter(Character::isAlphabetic)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());
    }
}
