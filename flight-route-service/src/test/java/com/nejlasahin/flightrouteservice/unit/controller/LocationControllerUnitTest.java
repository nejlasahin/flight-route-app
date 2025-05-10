package com.nejlasahin.flightrouteservice.unit.controller;

import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.controller.LocationController;
import com.nejlasahin.flightrouteservice.dto.request.LocationRequestDto;
import com.nejlasahin.flightrouteservice.dto.response.LocationResponseDto;
import com.nejlasahin.flightrouteservice.general.RestResponse;
import com.nejlasahin.flightrouteservice.helper.TestLocationDataHelper;
import com.nejlasahin.flightrouteservice.service.LocationService;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationControllerUnitTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private LocationRequestDto requestDto;
    private LocationResponseDto responseDto;

    @BeforeEach
    public void setUp() {
        requestDto = TestLocationDataHelper.createLocationRequestDto();
        responseDto = TestLocationDataHelper.createLocationResponseDto();
    }

    @Test
    public void getAll_WhenValidRequest_ShouldReturnResponse() {
        when(locationService.getAll()).thenReturn(List.of(responseDto));

        ResponseEntity<RestResponse<List<LocationResponseDto>>> responseEntity = locationController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().getData().isEmpty());
    }

    @Test
    public void save_WhenValidRequest_ShouldReturnCreatedResponse() {
        when(locationService.save(any(LocationRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<RestResponse<LocationResponseDto>> responseEntity = locationController.save(requestDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseDto.getName(), responseEntity.getBody().getData().getName());
    }

    @Test
    public void update_WhenValidRequest_ShouldReturnUpdatedResponse() {
        long id = 1L;
        when(locationService.update(any(LocationRequestDto.class), eq(id))).thenReturn(responseDto);

        ResponseEntity<RestResponse<LocationResponseDto>> responseEntity = locationController.update(requestDto, id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseDto.getName(), responseEntity.getBody().getData().getName());
    }

    @Test
    public void delete_WhenValidRequest_ShouldReturnDeletedResponse() {
        long id = 1L;
        doNothing().when(locationService).deleteById(id);

        ResponseEntity<RestResponse<String>> responseEntity = locationController.delete(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(String.format(SuccessMessages.LOCATION_DELETED_WITH_ID, id), responseEntity.getBody().getMessage());
    }

    @Test
    public void getById_WhenValidRequest_ShouldReturnLocationResponse() {
        long id = 1L;
        when(locationService.getById(id)).thenReturn(responseDto);

        ResponseEntity<RestResponse<LocationResponseDto>> responseEntity = locationController.getById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseDto.getName(), responseEntity.getBody().getData().getName());
    }
}
