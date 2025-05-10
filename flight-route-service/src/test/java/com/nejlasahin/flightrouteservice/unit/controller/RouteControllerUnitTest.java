package com.nejlasahin.flightrouteservice.unit.controller;

import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.controller.RouteController;
import com.nejlasahin.flightrouteservice.dto.response.RouteResponseDto;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.general.RestResponse;
import com.nejlasahin.flightrouteservice.helper.TestTransportationDataHelper;
import com.nejlasahin.flightrouteservice.service.RouteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteControllerUnitTest {

    @InjectMocks
    private RouteController routeController;

    @Mock
    private RouteService routeService;

    @Test
    public void getAllByDate_WhenValidRequest_ShouldReturnResponse() {
        long originId = 1L;
        long destinationId = 2L;
        LocalDate travelDate = LocalDate.now().plusDays(1);
        RouteResponseDto responseDto = new RouteResponseDto();
        responseDto.setTransportations(List.of(TestTransportationDataHelper.createTransportationResponseDto(TransportationType.FLIGHT)));
        List<RouteResponseDto> responseDtoList = List.of(responseDto);

        when(routeService.getAllByDate(originId, destinationId, travelDate)).thenReturn(responseDtoList);

        ResponseEntity<RestResponse<List<RouteResponseDto>>> response = routeController.getAllByDate(originId, destinationId, travelDate);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(responseDtoList, response.getBody().getData());
        assertEquals(String.format(SuccessMessages.ROUTES_RETRIEVED_WITH_TRAVEL_DATE, travelDate), response.getBody().getMessage());
        verify(routeService).getAllByDate(originId, destinationId, travelDate);
    }
}
