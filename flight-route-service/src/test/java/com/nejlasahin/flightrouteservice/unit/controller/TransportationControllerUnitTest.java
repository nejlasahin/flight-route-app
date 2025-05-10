package com.nejlasahin.flightrouteservice.unit.controller;

import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.controller.TransportationController;
import com.nejlasahin.flightrouteservice.dto.request.TransportationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.TransportationResponseDto;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.general.RestResponse;
import com.nejlasahin.flightrouteservice.helper.TestTransportationDataHelper;
import com.nejlasahin.flightrouteservice.service.TransportationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransportationControllerUnitTest {

    @Mock
    private TransportationService transportationService;

    @InjectMocks
    private TransportationController transportationController;

    private TransportationRequestDto requestDto;
    private TransportationResponseDto responseDto;
    private long id;

    @BeforeEach
    public void setUp() {
        responseDto = TestTransportationDataHelper.createTransportationResponseDto(TransportationType.FLIGHT);
        requestDto = TestTransportationDataHelper.createTransportationRequestDto(
                responseDto.getOriginLocation().getId(),
                responseDto.getDestinationLocation().getId(),
                TransportationType.FLIGHT
        );
        id = 1L;
    }

    @Test
    public void getAll_shouldReturnListOfTransportations() {
        when(transportationService.getAll()).thenReturn(List.of(responseDto));

        ResponseEntity<RestResponse<List<TransportationResponseDto>>> response = transportationController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getData().isEmpty());
        assertEquals(SuccessMessages.TRANSPORTATIONS_RETRIEVED, response.getBody().getMessage());
        verify(transportationService).getAll();
    }

    @Test
    public void save_whenValidRequest_shouldReturnCreatedTransportation() {
        when(transportationService.save(any())).thenReturn(responseDto);

        ResponseEntity<RestResponse<TransportationResponseDto>> response = transportationController.save(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto.getTransportationType(), response.getBody().getData().getTransportationType());
        assertEquals(SuccessMessages.TRANSPORTATION_CREATED, response.getBody().getMessage());
        verify(transportationService).save(any());
    }

    @Test
    public void update_whenValidRequest_shouldReturnUpdatedTransportation() {
        when(transportationService.update(any(), eq(id))).thenReturn(responseDto);

        ResponseEntity<RestResponse<TransportationResponseDto>> response = transportationController.update(requestDto, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto.getTransportationType(), response.getBody().getData().getTransportationType());
        assertEquals(SuccessMessages.TRANSPORTATION_UPDATED, response.getBody().getMessage());
        verify(transportationService).update(any(), eq(id));
    }

    @Test
    public void delete_whenValidId_shouldReturnDeleteSuccessfully() {
        doNothing().when(transportationService).deleteById(id);

        ResponseEntity<RestResponse<String>> response = transportationController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(String.format(SuccessMessages.TRANSPORTATION_DELETED_WITH_ID, id), response.getBody().getMessage());
        verify(transportationService).deleteById(id);
    }

    @Test
    public void getById_whenValidId_shouldReturnTransportation() {
        when(transportationService.getById(id)).thenReturn(responseDto);

        ResponseEntity<RestResponse<TransportationResponseDto>> response = transportationController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto.getTransportationType(), response.getBody().getData().getTransportationType());
        assertEquals(String.format(SuccessMessages.TRANSPORTATION_RETRIEVED_WITH_ID, id), response.getBody().getMessage());
        verify(transportationService).getById(id);
    }
}
