package com.nejlasahin.flightrouteservice.unit.service;

import com.nejlasahin.flightrouteservice.constants.ExceptionMessages;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.dto.response.RouteResponseDto;
import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.exception.ParameterValidationException;
import com.nejlasahin.flightrouteservice.exception.SameLocationException;
import com.nejlasahin.flightrouteservice.helper.TestLocationDataHelper;
import com.nejlasahin.flightrouteservice.helper.TestTransportationDataHelper;
import com.nejlasahin.flightrouteservice.service.LocationService;
import com.nejlasahin.flightrouteservice.service.TransportationService;
import com.nejlasahin.flightrouteservice.service.impl.RouteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteServiceUnitTest {

    private final LocalDate travelDate = LocalDate.now().plusDays(1);
    @Mock
    private TransportationService transportationService;
    @Mock
    private LocationService locationService;
    @InjectMocks
    private RouteServiceImpl routeService;

    @Test
    public void getAllByDate_whenValidData_shouldReturnRoutes() {
        TransportationResponseDto flight = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.FLIGHT);
        when(transportationService.getAllByOperatingDay(anyInt())).thenReturn(List.of(flight));
        when(locationService.getLocationCodeById(flight.getOriginLocation().getId())).thenReturn(flight.getOriginLocation().getLocationCode());
        when(locationService.getLocationCodeById(flight.getDestinationLocation().getId())).thenReturn(flight.getDestinationLocation().getLocationCode());

        List<RouteResponseDto> result = routeService.getAllByDate(flight.getOriginLocation().getId(), flight.getDestinationLocation().getId(), travelDate);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTransportations().size());
    }

    @Test
    public void getAllByDate_withBeforeTransfer_shouldReturnTransferRoute() {
        LocationResponseDto origin = TestLocationDataHelper.createLocationResponseDto();
        LocationResponseDto transfer = TestLocationDataHelper.createLocationResponseDto();
        LocationResponseDto destination = TestLocationDataHelper.createLocationResponseDto();

        TransportationResponseDto bus = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.BUS, origin, transfer);
        TransportationResponseDto flight = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.FLIGHT, transfer, destination);

        when(transportationService.getAllByOperatingDay(anyInt())).thenReturn(List.of(bus, flight));
        when(locationService.getLocationCodeById(origin.getId())).thenReturn(origin.getLocationCode());
        when(locationService.getLocationCodeById(destination.getId())).thenReturn(destination.getLocationCode());

        List<RouteResponseDto> result = routeService.getAllByDate(origin.getId(), destination.getId(), travelDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getTransportations().size());
    }

    @Test
    public void getAllByDate_withAfterTransfer_shouldReturnTransferRoute() {
        LocationResponseDto origin = TestLocationDataHelper.createLocationResponseDto();
        LocationResponseDto transfer = TestLocationDataHelper.createLocationResponseDto();
        LocationResponseDto destination = TestLocationDataHelper.createLocationResponseDto();

        TransportationResponseDto flight = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.FLIGHT, origin, transfer);
        TransportationResponseDto subway = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.BUS, transfer, destination);

        when(transportationService.getAllByOperatingDay(anyInt())).thenReturn(List.of(flight, subway));
        when(locationService.getLocationCodeById(origin.getId())).thenReturn(origin.getLocationCode());
        when(locationService.getLocationCodeById(destination.getId())).thenReturn(destination.getLocationCode());

        List<RouteResponseDto> result = routeService.getAllByDate(origin.getId(), destination.getId(), travelDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getTransportations().size());
    }

    @Test
    public void getAllByDate_withBeforeAndAfterTransfer_shouldReturnTripleTransferRoute() {
        LocationResponseDto origin = TestLocationDataHelper.createLocationResponseDto();
        LocationResponseDto firstTransfer = TestLocationDataHelper.createLocationResponseDto();
        LocationResponseDto secondTransfer = TestLocationDataHelper.createLocationResponseDto();
        LocationResponseDto destination = TestLocationDataHelper.createLocationResponseDto();

        TransportationResponseDto bus = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.BUS, origin, firstTransfer);
        TransportationResponseDto flight = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.FLIGHT, firstTransfer, secondTransfer);
        TransportationResponseDto uber = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.UBER, secondTransfer, destination);

        when(transportationService.getAllByOperatingDay(anyInt())).thenReturn(List.of(bus, flight, uber));
        when(locationService.getLocationCodeById(origin.getId())).thenReturn(origin.getLocationCode());
        when(locationService.getLocationCodeById(destination.getId())).thenReturn(destination.getLocationCode());

        List<RouteResponseDto> result = routeService.getAllByDate(origin.getId(), destination.getId(), travelDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getTransportations().size());
    }

    @Test
    public void getAllByDate_whenNoTransportations_shouldReturnEmptyList() {
        when(transportationService.getAllByOperatingDay(anyInt())).thenReturn(List.of());

        List<RouteResponseDto> result = routeService.getAllByDate(1L, 2L, travelDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllByDate_whenOriginAndDestinationAreSame_shouldThrowSameLocationException() {
        SameLocationException exception = assertThrows(SameLocationException.class, () ->
                routeService.getAllByDate(1L, 1L, travelDate));
        assertEquals(ExceptionMessages.SAME_LOCATION_EXCEPTION, exception.getMessage());
    }

    @Test
    public void getAllByDate_whenParametersAreMissing_shouldThrowParameterValidationException() {
        ParameterValidationException exception = assertThrows(ParameterValidationException.class, () ->
                routeService.getAllByDate(null, null, null));
        assertEquals(ExceptionMessages.TRANSPORTATION_PARAMETERS_MISSING, exception.getMessage());
    }

    @Test
    public void getAllByDate_whenTravelDateIsInThePast_shouldThrowParameterValidationException() {
        ParameterValidationException exception = assertThrows(ParameterValidationException.class, () ->
                routeService.getAllByDate(1L, 2L, LocalDate.now().minusDays(1)));
        assertEquals(ExceptionMessages.TRAVEL_DATE_IN_PAST, exception.getMessage());
    }
}
