package com.nejlasahin.flightrouteservice.unit.service;

import com.nejlasahin.flightrouteservice.dto.request.LocationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.exception.AlreadyExistException;
import com.nejlasahin.flightrouteservice.exception.NotFoundException;
import com.nejlasahin.flightrouteservice.helper.TestLocationDataHelper;
import com.nejlasahin.flightrouteservice.repository.LocationRepository;
import com.nejlasahin.flightrouteservice.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationServiceUnitTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;
    private LocationRequestDto requestDto;

    @BeforeEach
    public void setUp() {
        location = TestLocationDataHelper.createLocationEntity();
        requestDto = TestLocationDataHelper.createLocationRequestDto(location);
    }

    @Test
    public void getAll_whenLocationsExist_shouldReturnLocationResponseDtoList() {
        List<Location> locationList = List.of(location);
        when(locationRepository.findAll()).thenReturn(locationList);

        List<LocationResponseDto> result = locationService.getAll();

        assertEquals(locationList.size(), result.size());
        assertEquals(location.getLocationCode(), result.get(0).getLocationCode());
    }

    @Test
    public void save_whenLocationDoesNotExist_shouldReturnSavedLocationResponseDto() {
        when(locationRepository.existsByLocationCode(location.getLocationCode())).thenReturn(false);
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationResponseDto result = locationService.save(requestDto);

        assertEquals(location.getLocationCode(), result.getLocationCode());
    }

    @Test
    public void save_whenLocationCodeAlreadyExists_shouldThrowAlreadyExistException() {
        when(locationRepository.existsByLocationCode(requestDto.getLocationCode())).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> locationService.save(requestDto));
    }

    @Test
    public void update_whenLocationExistsAndCodeIsUnique_shouldUpdateAndReturnLocationResponseDto() {
        Location existing = TestLocationDataHelper.createLocationEntityWithId();
        when(locationRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(locationRepository.existsByLocationCodeAndIdNot(location.getLocationCode(), existing.getId())).thenReturn(false);
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationResponseDto result = locationService.update(requestDto, existing.getId());

        assertEquals(location.getLocationCode(), result.getLocationCode());
    }

    @Test
    public void update_whenLocationExistsButCodeAlreadyExists_shouldThrowAlreadyExistException() {
        Location existing = TestLocationDataHelper.createLocationEntityWithId();
        when(locationRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(locationRepository.existsByLocationCodeAndIdNot(requestDto.getLocationCode(), existing.getId())).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> locationService.update(requestDto, existing.getId()));
    }

    @Test
    public void getById_whenLocationExists_shouldReturnLocationResponseDto() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));

        LocationResponseDto result = locationService.getById(location.getId());

        assertEquals(location.getLocationCode(), result.getLocationCode());
    }

    @Test
    public void getById_whenLocationDoesNotExist_shouldThrowNotFoundException() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> locationService.getById(location.getId()));
    }

    @Test
    public void deleteById_whenLocationExists_shouldDeleteLocation() {
        when(locationRepository.existsById(location.getId())).thenReturn(true);

        locationService.deleteById(location.getId());

        verify(locationRepository).deleteById(location.getId());
    }

    @Test
    public void deleteById_whenLocationDoesNotExist_shouldThrowNotFoundException() {
        when(locationRepository.existsById(location.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> locationService.deleteById(location.getId()));
    }

    @Test
    public void getLocationCodeById_whenLocationExists_shouldReturnLocationCode() {
        when(locationRepository.findLocationCodeById(location.getId())).thenReturn(location.getLocationCode());

        String code = locationService.getLocationCodeById(location.getId());

        assertEquals(location.getLocationCode(), code);
    }

    @Test
    public void getLocationCodeById_whenLocationDoesNotExist_shouldThrowNotFoundException() {
        when(locationRepository.findLocationCodeById(location.getId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> locationService.getLocationCodeById(location.getId()));
    }
}
