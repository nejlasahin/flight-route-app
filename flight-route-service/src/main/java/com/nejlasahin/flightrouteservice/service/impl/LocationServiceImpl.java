package com.nejlasahin.flightrouteservice.service.impl;

import com.nejlasahin.flightrouteservice.constants.ExceptionMessages;
import com.nejlasahin.flightrouteservice.converter.LocationConverter;
import com.nejlasahin.flightrouteservice.dto.request.LocationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.exception.AlreadyExistException;
import com.nejlasahin.flightrouteservice.exception.NotFoundException;
import com.nejlasahin.flightrouteservice.repository.LocationRepository;
import com.nejlasahin.flightrouteservice.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public List<LocationResponseDto> getAll() {
        List<Location> locations = locationRepository.findAll();
        return LocationConverter.toLocationResponseDtoList(locations);
    }

    @Override
    public LocationResponseDto save(LocationRequestDto requestDto) {
        validateLocation(requestDto.getLocationCode(), null);
        Location convertedLocation = LocationConverter.toLocation(requestDto);
        locationRepository.save(convertedLocation);
        return LocationConverter.toLocationResponseDto(convertedLocation);
    }

    @Override
    public LocationResponseDto update(LocationRequestDto requestDto, Long id) {
        Location location = getEntityById(id);
        validateLocation(requestDto.getLocationCode(), id);
        Location convertedLocation = LocationConverter.toLocation(location, requestDto);
        locationRepository.save(convertedLocation);
        return LocationConverter.toLocationResponseDto(convertedLocation);
    }

    @Override
    public void deleteById(Long id) {
        validateExistsById(id);
        locationRepository.deleteById(id);
    }

    @Override
    public LocationResponseDto getById(Long id) {
        Location location = getEntityById(id);
        return LocationConverter.toLocationResponseDto(location);
    }

    @Override
    public Location getEntityById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow((() -> new NotFoundException(String.format(ExceptionMessages.LOCATION_NOT_FOUND, id))));
    }

    @Override
    public String getLocationCodeById(Long id) {
        String locationCode = locationRepository.findLocationCodeById(id);
        if (locationCode == null) {
            throw new NotFoundException(String.format(ExceptionMessages.LOCATION_NOT_FOUND, id));
        }
        return locationCode;
    }

    @Override
    public void validateExistsById(Long id) {
        boolean isExistsById = locationRepository.existsById(id);
        if (!isExistsById) {
            throw new NotFoundException(String.format(ExceptionMessages.LOCATION_NOT_FOUND, id));
        }
    }

    private void validateLocation(String locationCode, Long excludeId) {
        boolean exists;
        if (excludeId == null) {
            exists = locationRepository.existsByLocationCode(locationCode);
        } else {
            exists = locationRepository.existsByLocationCodeAndIdNot(locationCode, excludeId);
        }

        if (exists) {
            throw new AlreadyExistException(String.format(ExceptionMessages.LOCATION_CODE_ALREADY_EXISTS, locationCode));
        }
    }
}
