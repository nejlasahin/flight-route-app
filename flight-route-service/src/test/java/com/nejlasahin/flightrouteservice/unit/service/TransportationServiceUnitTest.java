package com.nejlasahin.flightrouteservice.unit.service;

import com.nejlasahin.flightrouteservice.dto.request.TransportationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.entity.Transportation;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.exception.AlreadyExistException;
import com.nejlasahin.flightrouteservice.exception.NotFoundException;
import com.nejlasahin.flightrouteservice.exception.SameLocationException;
import com.nejlasahin.flightrouteservice.helper.TestLocationDataHelper;
import com.nejlasahin.flightrouteservice.helper.TestTransportationDataHelper;
import com.nejlasahin.flightrouteservice.repository.TransportationRepository;
import com.nejlasahin.flightrouteservice.service.LocationService;
import com.nejlasahin.flightrouteservice.service.impl.TransportationServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransportationServiceUnitTest {

    @Mock
    private TransportationRepository transportationRepository;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private TransportationServiceImpl transportationService;

    private TransportationRequestDto requestDto;
    private Transportation transportation;
    private Location originLocation;
    private Location destinationLocation;

    @BeforeEach
    public void setUp() {
        originLocation = TestLocationDataHelper.createLocationEntityWithId();
        destinationLocation = TestLocationDataHelper.createLocationEntityWithId();

        requestDto = TestTransportationDataHelper.createTransportationRequestDto(originLocation.getId(), destinationLocation.getId(), TransportationType.FLIGHT);

        transportation = TestTransportationDataHelper.createTransportationEntityWithId(originLocation, destinationLocation, TransportationType.FLIGHT, 7);
    }

    @Test
    public void getAll_whenTransportationExists_shouldReturnTransportationResponseDtoList() {
        when(transportationRepository.findAll()).thenReturn(List.of(transportation));

        var result = transportationService.getAll();

        assertEquals(1, result.size());
        verify(transportationRepository).findAll();
    }

    @Test
    public void getAllByOperatingDay_whenOperatingDayExists_shouldReturnTransportationResponseDtoList() {
        when(transportationRepository.findAllByOperatingDay(1)).thenReturn(List.of(transportation));

        var result = transportationService.getAllByOperatingDay(1);

        assertEquals(1, result.size());
        verify(transportationRepository).findAllByOperatingDay(1);
    }

    @Test
    public void getById_whenTransportationExists_shouldReturnTransportationResponseDto() {
        when(transportationRepository.findById(10L)).thenReturn(Optional.of(transportation));

        var result = transportationService.getById(10L);

        assertEquals(TransportationType.FLIGHT, result.getTransportationType());
    }

    @Test
    public void getById_whenTransportationDoesNotExist_shouldThrowNotFoundException() {
        when(transportationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transportationService.getById(99L));
    }

    @Test
    public void deleteById_whenTransportationExists_shouldDeleteTransportation() {
        when(transportationRepository.existsById(10L)).thenReturn(true);

        transportationService.deleteById(10L);

        verify(transportationRepository).deleteById(10L);
    }

    @Test
    public void deleteById_whenTransportationDoesNotExist_shouldThrowNotFoundException() {
        when(transportationRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> transportationService.deleteById(99L));
    }

    @Test
    public void save_whenTransportationIsValid_shouldCreateNewTransportation() {
        when(locationService.getEntityById(originLocation.getId())).thenReturn(originLocation);
        when(locationService.getEntityById(destinationLocation.getId())).thenReturn(destinationLocation);
        when(transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationType(
                originLocation, destinationLocation, TransportationType.FLIGHT)).thenReturn(false);

        var result = transportationService.save(requestDto);

        assertEquals(TransportationType.FLIGHT, result.getTransportationType());
    }

    @Test
    public void save_whenOriginAndDestinationAreSame_shouldThrowSameLocationException() {
        requestDto.setDestinationLocationId(originLocation.getId());
        requestDto.setOriginLocationId(originLocation.getId());
        when(locationService.getEntityById(originLocation.getId())).thenReturn(originLocation);

        assertThrows(SameLocationException.class, () -> transportationService.save(requestDto));
    }

    @Test
    public void save_whenTransportationAlreadyExists_shouldThrowAlreadyExistException() {
        when(locationService.getEntityById(originLocation.getId())).thenReturn(originLocation);
        when(locationService.getEntityById(destinationLocation.getId())).thenReturn(destinationLocation);
        when(transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationType(
                originLocation, destinationLocation, TransportationType.FLIGHT)).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> transportationService.save(requestDto));
    }

    @Test
    public void update_whenTransportationIsValid_shouldUpdateTransportation() {
        Transportation existing = TestTransportationDataHelper.createTransportationEntityWithId(originLocation, destinationLocation, TransportationType.FLIGHT, 7);
        requestDto.setTransportationType(TransportationType.UBER.toString());
        when(transportationRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(locationService.getEntityById(originLocation.getId())).thenReturn(originLocation);
        when(locationService.getEntityById(destinationLocation.getId())).thenReturn(destinationLocation);
        when(transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationTypeAndIdNot(
                originLocation, destinationLocation, TransportationType.UBER, existing.getId())).thenReturn(false);
        when(transportationRepository.save(existing)).thenReturn(existing);

        TransportationResponseDto result = transportationService.update(requestDto, existing.getId());

        assertEquals(TransportationType.UBER, result.getTransportationType());
    }

    @Test
    public void update_whenTransportationConflictExists_shouldThrowAlreadyExistException() {
        Transportation existing = TestTransportationDataHelper.createTransportationEntityWithId(originLocation, destinationLocation, TransportationType.FLIGHT, 7);

        when(transportationRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(locationService.getEntityById(originLocation.getId())).thenReturn(originLocation);
        when(locationService.getEntityById(destinationLocation.getId())).thenReturn(destinationLocation);
        when(transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationTypeAndIdNot(
                originLocation, destinationLocation, TransportationType.FLIGHT, existing.getId())).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> transportationService.update(requestDto, existing.getId()));
    }
}