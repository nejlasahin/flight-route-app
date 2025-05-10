package com.nejlasahin.flightrouteservice.service.impl;

import com.nejlasahin.flightrouteservice.constants.ExceptionMessages;
import com.nejlasahin.flightrouteservice.converter.TransportationConverter;
import com.nejlasahin.flightrouteservice.dto.request.TransportationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.entity.Transportation;
import com.nejlasahin.flightrouteservice.entity.TransportationOperatingDay;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.exception.AlreadyExistException;
import com.nejlasahin.flightrouteservice.exception.NotFoundException;
import com.nejlasahin.flightrouteservice.exception.SameLocationException;
import com.nejlasahin.flightrouteservice.repository.TransportationRepository;
import com.nejlasahin.flightrouteservice.service.LocationService;
import com.nejlasahin.flightrouteservice.service.TransportationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {
    private final TransportationRepository transportationRepository;
    private final LocationService locationService;

    @Override
    public List<TransportationResponseDto> getAll() {
        List<Transportation> transportations = transportationRepository.findAll();
        return TransportationConverter.toTransportationResponseDtoList(transportations);
    }

    @Override
    public List<TransportationResponseDto> getAllByOperatingDay(Integer operatingDay) {
        List<Transportation> transportations = transportationRepository.findAllByOperatingDay(operatingDay);
        return TransportationConverter.toTransportationResponseDtoList(transportations);
    }

    @Override
    public TransportationResponseDto save(TransportationRequestDto requestDto) {
        Transportation transportation = new Transportation();
        constructTransportation(requestDto, transportation, null);
        transportationRepository.save(transportation);
        return TransportationConverter.toTransportationResponseDto(transportation);
    }

    @Override
    public TransportationResponseDto update(TransportationRequestDto requestDto, Long id) {
        Transportation transportation = getEntityById(id);
        constructTransportation(requestDto, transportation, id);
        Transportation updated = transportationRepository.save(transportation);
        return TransportationConverter.toTransportationResponseDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        boolean isExistsById = transportationRepository.existsById(id);
        if (!isExistsById) {
            throw new NotFoundException(String.format(ExceptionMessages.TRANSPORTATION_NOT_FOUND, id));
        }
        transportationRepository.deleteById(id);
    }

    @Override
    public TransportationResponseDto getById(Long id) {
        Transportation transportation = getEntityById(id);
        return TransportationConverter.toTransportationResponseDto(transportation);
    }

    private Transportation getEntityById(Long id) {
        return transportationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.TRANSPORTATION_NOT_FOUND, id)));
    }

    private void constructTransportation(TransportationRequestDto requestDto, Transportation transportation, Long excludeId) {
        Location originLocation = locationService.getEntityById(requestDto.getOriginLocationId());
        Location destinationLocation = locationService.getEntityById(requestDto.getDestinationLocationId());
        TransportationType type = TransportationType.valueOf(requestDto.getTransportationType());

        validateTransportation(originLocation, destinationLocation, type, excludeId);
        prepareTransportation(requestDto, transportation);
    }

    private void validateTransportation(Location originLocation, Location destinationLocation, TransportationType type, Long excludeId) {
        if (originLocation.getId().equals(destinationLocation.getId())) {
            throw new SameLocationException(ExceptionMessages.SAME_LOCATION_EXCEPTION);
        }

        boolean exists;
        if (excludeId == null) {
            exists = transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationType(originLocation, destinationLocation, type);
        } else {
            exists = transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationTypeAndIdNot(originLocation, destinationLocation, type, excludeId);
        }

        if (exists) {
            throw new AlreadyExistException(String.format(ExceptionMessages.TRANSPORTATION_ALREADY_EXISTS, originLocation.getId(), destinationLocation.getId(), type.toString().toLowerCase(Locale.ROOT)));
        }
    }

    private void prepareTransportation(TransportationRequestDto requestDto, Transportation transportation) {
        Location destinationLocation = locationService.getEntityById(requestDto.getDestinationLocationId());
        Location originLocation = locationService.getEntityById(requestDto.getOriginLocationId());
        TransportationType type = TransportationType.valueOf(requestDto.getTransportationType());

        transportation.setDestinationLocation(destinationLocation);
        transportation.setOriginLocation(originLocation);
        transportation.setTransportationType(type);

        List<TransportationOperatingDay> newOperatingDays = requestDto.getOperatingDays().stream()
                .map(day -> new TransportationOperatingDay(null, day, transportation))
                .toList();

        transportation.getOperatingDays().clear();
        transportation.getOperatingDays().addAll(newOperatingDays);
    }
}
