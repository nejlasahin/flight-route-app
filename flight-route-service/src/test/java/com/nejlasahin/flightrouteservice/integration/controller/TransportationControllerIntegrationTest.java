package com.nejlasahin.flightrouteservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nejlasahin.flightrouteservice.constants.EndpointPaths;
import com.nejlasahin.flightrouteservice.constants.ExceptionMessages;
import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.constants.ValidationMessages;
import com.nejlasahin.flightrouteservice.dto.request.TransportationRequestDto;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.entity.Transportation;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.helper.TestLocationDataHelper;
import com.nejlasahin.flightrouteservice.helper.TestTransportationDataHelper;
import com.nejlasahin.flightrouteservice.repository.LocationRepository;
import com.nejlasahin.flightrouteservice.repository.TransportationRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransportationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransportationRepository transportationRepository;

    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        transportationRepository.deleteAll();
    }

    @Test
    public void getAll_shouldReturnListOfTransportations() throws Exception {
        performGetRequest()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value(SuccessMessages.TRANSPORTATIONS_RETRIEVED));
    }

    @Test
    public void save_whenValidRequest_shouldReturnCreatedTransportation() throws Exception {
        Location originLocation = createAndSaveLocation();
        Location destinationLocation = createAndSaveLocation();
        TransportationRequestDto requestDto = createTransportationRequest(originLocation, destinationLocation, TransportationType.FLIGHT);

        performSaveRequest(requestDto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(SuccessMessages.TRANSPORTATION_CREATED));
    }

    @Test
    public void save_whenTransportationTypeIsInvalid_shouldReturnBadRequest() throws Exception {
        Location originLocation = createAndSaveLocation();
        Location destinationLocation = createAndSaveLocation();
        TransportationRequestDto requestDto = createTransportationRequest(originLocation, destinationLocation, TransportationType.FLIGHT);
        requestDto.setTransportationType("INVALID_TRANSPORTATION_TYPE");

        performSaveRequest(requestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.message", Matchers.containsString(ValidationMessages.INVALID_TRANSPORTATION_TYPE)));
    }

    @Test
    public void save_whenOriginLocationAndDestinationLocationAreSame_shouldReturnSameLocationException() throws Exception {
        Location location = createAndSaveLocation();
        TransportationRequestDto requestDto = createTransportationRequest(location, location, TransportationType.FLIGHT);

        performSaveRequest(requestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.message", Matchers.containsString(ExceptionMessages.SAME_LOCATION_EXCEPTION)));
    }

    @Test
    public void save_whenSameTransportationAlreadyExists_shouldReturnInvalidTransportationException() throws Exception {
        Location originLocation = createAndSaveLocation();
        Location destinationLocation = createAndSaveLocation();
        TransportationRequestDto requestDto = createTransportationRequest(originLocation, destinationLocation, TransportationType.FLIGHT);
        TestTransportationDataHelper.createAndSaveTransportation(originLocation, destinationLocation, TransportationType.FLIGHT, 7, transportationRepository);

        performSaveRequest(requestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.message", Matchers.containsString(String.format(ExceptionMessages.TRANSPORTATION_ALREADY_EXISTS, requestDto.getOriginLocationId(), requestDto.getDestinationLocationId(), requestDto.getTransportationType().toLowerCase(Locale.ROOT)))));
    }

    @Test
    public void update_whenValidRequest_shouldReturnUpdatedTransportation() throws Exception {
        Location originLocation = createAndSaveLocation();
        Location destinationLocation = createAndSaveLocation();
        Transportation transportation = TestTransportationDataHelper.createAndSaveTransportation(originLocation, destinationLocation, TransportationType.FLIGHT, 7, transportationRepository);
        TransportationRequestDto requestDto = createTransportationRequest(originLocation, destinationLocation, TransportationType.BUS);

        performUpdateRequest(transportation.getId(), requestDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessages.TRANSPORTATION_UPDATED))
                .andExpect(jsonPath("$.data.transportation_type").value(requestDto.getTransportationType()));
    }

    @Test
    public void update_whenInvalidId_shouldReturnNotFoundException() throws Exception {
        long invalidId = 999L;
        Location originLocation = createAndSaveLocation();
        Location destinationLocation = createAndSaveLocation();
        TransportationRequestDto requestDto = createTransportationRequest(originLocation, destinationLocation, TransportationType.BUS);

        performUpdateRequest(invalidId, requestDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(String.format(ExceptionMessages.TRANSPORTATION_NOT_FOUND, invalidId)));
    }

    @Test
    public void delete_whenValidId_shouldReturnDeleteSuccessfully() throws Exception {
        Location originLocation = createAndSaveLocation();
        Location destinationLocation = createAndSaveLocation();
        Transportation transportation = TestTransportationDataHelper.createAndSaveTransportation(originLocation, destinationLocation, TransportationType.FLIGHT, 7, transportationRepository);

        performDeleteRequest(transportation.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(String.format(SuccessMessages.TRANSPORTATION_DELETED_WITH_ID, transportation.getId())));
    }

    @Test
    public void delete_whenInvalidId_shouldReturnNotFoundException() throws Exception {
        long invalidId = 999L;
        performDeleteRequest(invalidId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(String.format(ExceptionMessages.TRANSPORTATION_NOT_FOUND, invalidId)));
    }

    @Test
    public void getById_whenValidId_shouldReturnTransportation() throws Exception {
        Location originLocation = createAndSaveLocation();
        Location destinationLocation = createAndSaveLocation();
        Transportation transportation = TestTransportationDataHelper.createAndSaveTransportation(originLocation, destinationLocation, TransportationType.FLIGHT, 7, transportationRepository);

        performGetByIdRequest(transportation.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.origin_location.id").value(transportation.getOriginLocation().getId()))
                .andExpect(jsonPath("$.data.destination_location.id").value(transportation.getDestinationLocation().getId()));
    }

    @Test
    public void getById_whenInvalidId_shouldReturnNotFoundException() throws Exception {
        long invalidId = 999L;
        performGetByIdRequest(invalidId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(String.format(ExceptionMessages.TRANSPORTATION_NOT_FOUND, invalidId)));
    }

    private Location createAndSaveLocation() {
        return TestLocationDataHelper.createAndSaveLocation(locationRepository);
    }

    private TransportationRequestDto createTransportationRequest(Location originLocation, Location destinationLocation, TransportationType type) {
        return TestTransportationDataHelper.createTransportationRequestDto(originLocation.getId(), destinationLocation.getId(), type);
    }

    private ResultActions performSaveRequest(TransportationRequestDto requestDto) throws Exception {
        return mockMvc.perform(post(EndpointPaths.TRANSPORTATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
    }

    private ResultActions performUpdateRequest(long id, TransportationRequestDto requestDto) throws Exception {
        return mockMvc.perform(put(EndpointPaths.TRANSPORTATIONS + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
    }

    private ResultActions performDeleteRequest(long id) throws Exception {
        return mockMvc.perform(delete(EndpointPaths.TRANSPORTATIONS + "/" + id)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performGetByIdRequest(long id) throws Exception {
        return mockMvc.perform(get(EndpointPaths.TRANSPORTATIONS + "/" + id)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performGetRequest() throws Exception {
        return mockMvc.perform(get(EndpointPaths.TRANSPORTATIONS)
                .contentType(MediaType.APPLICATION_JSON));
    }
}
