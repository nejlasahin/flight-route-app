package com.nejlasahin.flightrouteservice.service.impl;

import com.nejlasahin.flightrouteservice.constants.ExceptionMessages;
import com.nejlasahin.flightrouteservice.dto.response.RouteResponseDto;
import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.exception.ParameterValidationException;
import com.nejlasahin.flightrouteservice.exception.SameLocationException;
import com.nejlasahin.flightrouteservice.service.LocationService;
import com.nejlasahin.flightrouteservice.service.RouteService;
import com.nejlasahin.flightrouteservice.service.TransportationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final TransportationService transportationService;
    private final LocationService locationService;

    public List<RouteResponseDto> getAllByDate(Long originLocationId, Long destinationLocationId, LocalDate travelDate) {
        validateInputs(originLocationId, destinationLocationId, travelDate);

        int dayOfWeek = travelDate.getDayOfWeek().getValue();
        List<TransportationResponseDto> allTransportations = transportationService.getAllByOperatingDay(dayOfWeek);
        if (allTransportations.isEmpty()) {
            return Collections.emptyList();
        }
        String originCode = locationService.getLocationCodeById(originLocationId);
        String destinationCode = locationService.getLocationCodeById(destinationLocationId);
        List<TransportationResponseDto> flights = filterFlights(allTransportations);
        return constructRoutes(flights, allTransportations, originCode, destinationCode);
    }

    private void validateInputs(Long originLocationId, Long destinationLocationId, LocalDate travelDate) {
        if (originLocationId == null || destinationLocationId == null || travelDate == null) {
            throw new ParameterValidationException(ExceptionMessages.TRANSPORTATION_PARAMETERS_MISSING);
        }
        if (travelDate.isBefore(LocalDate.now())) {
            throw new ParameterValidationException(ExceptionMessages.TRAVEL_DATE_IN_PAST);
        }
        locationService.validateExistsById(originLocationId);
        locationService.validateExistsById(destinationLocationId);
        if (originLocationId.equals(destinationLocationId)) {
            throw new SameLocationException(ExceptionMessages.SAME_LOCATION_EXCEPTION);
        }
    }

    private List<RouteResponseDto> constructRoutes(List<TransportationResponseDto> flights, List<TransportationResponseDto> allTransportations, String originCode, String destinationCode) {
        List<RouteResponseDto> response = new ArrayList<>();

        for (TransportationResponseDto flight : flights) {
            String flightOrigin = flight.getOriginLocation().getLocationCode();
            String flightDestination = flight.getDestinationLocation().getLocationCode();

            if (flightOrigin.equals(originCode) && flightDestination.equals(destinationCode)) {
                response.add(new RouteResponseDto(List.of(flight)));
            }

            List<TransportationResponseDto> beforeTransfers = findTransfers(allTransportations, originCode, flightOrigin);
            for (TransportationResponseDto before : beforeTransfers) {
                if (flightDestination.equals(destinationCode)) {
                    response.add(new RouteResponseDto(List.of(before, flight)));
                }
            }

            List<TransportationResponseDto> afterTransfers = findTransfers(allTransportations, flightDestination, destinationCode);
            for (TransportationResponseDto after : afterTransfers) {
                if (flightOrigin.equals(originCode)) {
                    response.add(new RouteResponseDto(List.of(flight, after)));
                }
            }

            for (TransportationResponseDto before : beforeTransfers) {
                for (TransportationResponseDto after : afterTransfers) {
                    response.add(new RouteResponseDto(List.of(before, flight, after)));
                }
            }
        }
        return response;
    }

    private List<TransportationResponseDto> filterFlights(List<TransportationResponseDto> allTransportations) {
        return allTransportations.stream()
                .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                .collect(Collectors.toList());
    }

    private List<TransportationResponseDto> findTransfers(List<TransportationResponseDto> allTransportations, String fromCode, String toCode) {
        return allTransportations.stream()
                .filter(t -> t.getTransportationType() != TransportationType.FLIGHT)
                .filter(t -> t.getOriginLocation().getLocationCode().equals(fromCode))
                .filter(t -> t.getDestinationLocation().getLocationCode().equals(toCode))
                .collect(Collectors.toList());
    }
}