package com.nejlasahin.flightrouteservice.service;


import com.nejlasahin.flightrouteservice.dto.request.TransportationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;

import java.util.List;

public interface TransportationService {

    List<TransportationResponseDto> getAll();

    List<TransportationResponseDto> getAllByOperatingDay(Integer operatingDay);

    TransportationResponseDto save(TransportationRequestDto requestDto);

    TransportationResponseDto update(TransportationRequestDto requestDto, Long id);

    void deleteById(Long id);

    TransportationResponseDto getById(Long id);
}
