package com.nejlasahin.flightrouteservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nejlasahin.flightrouteservice.constants.EndpointPaths;
import com.nejlasahin.flightrouteservice.constants.ExceptionMessages;
import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.constants.ValidationMessages;
import com.nejlasahin.flightrouteservice.dto.request.LocationRequestDto;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.helper.TestLocationDataHelper;
import com.nejlasahin.flightrouteservice.repository.LocationRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocationRepository locationRepository;

    private LocationRequestDto requestDto;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        requestDto = TestLocationDataHelper.createLocationRequestDto();
    }

    @Test
    public void getAll_shouldReturnListOfLocations() throws Exception {
        performGetRequest()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void save_whenValidRequest_shouldReturnCreatedLocation() throws Exception {
        performSaveRequest(requestDto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value(requestDto.getName()))
                .andExpect(jsonPath("$.data.country").value(requestDto.getCountry()));
    }

    @Test
    public void save_whenLocationCodeIsNull_shouldReturnMethodArgumentNotValidException() throws Exception {
        requestDto.setLocationCode(null);

        performSaveRequest(requestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.message", Matchers.containsString(ValidationMessages.LOCATION_CODE_NOT_BLANK)));
    }

    @Test
    public void save_whenLocationCodeAlreadyExists_shouldReturnAlreadyExistException() throws Exception {
        String locationCode = "TEST_LOCATION_CODE";
        createAndSaveLocationWhenLocationCode(locationCode);
        requestDto.setLocationCode(locationCode);

        performSaveRequest(requestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.message").value(String.format(ExceptionMessages.LOCATION_CODE_ALREADY_EXISTS, locationCode)));
    }

    @Test
    public void update_whenValidId_shouldUpdateAndReturnLocation() throws Exception {
        Location location = createAndSaveLocation();

        performUpdateRequest(location.getId(), requestDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(requestDto.getName()))
                .andExpect(jsonPath("$.data.country").value(requestDto.getCountry()));
    }

    @Test
    public void update_whenInvalidId_shouldReturnNotFoundException() throws Exception {
        long invalidId = 999L;

        performUpdateRequest(invalidId, requestDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(String.format(ExceptionMessages.LOCATION_NOT_FOUND, invalidId)));
    }

    @Test
    public void update_whenLocationCodeAlreadyExists_shouldReturnAlreadyExistException() throws Exception {
        String locationCode = "TEST_LOCATION_CODE";
        createAndSaveLocationWhenLocationCode(locationCode);
        requestDto.setLocationCode(locationCode);
        Location location = createAndSaveLocation();

        performUpdateRequest(location.getId(), requestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(String.format(ExceptionMessages.LOCATION_CODE_ALREADY_EXISTS, locationCode)));
    }

    @Test
    public void delete_whenValidId_shouldReturnDeleteSuccessfully() throws Exception {
        Location location = createAndSaveLocation();

        performDeleteRequest(location.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(String.format(SuccessMessages.LOCATION_DELETED_WITH_ID, location.getId())));
    }

    @Test
    public void delete_whenInvalidId_shouldReturnNotFoundException() throws Exception {
        long invalidId = 999L;

        performDeleteRequest(invalidId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(String.format(ExceptionMessages.LOCATION_NOT_FOUND, invalidId)));
    }

    @Test
    public void getById_whenValidId_shouldReturnLocation() throws Exception {
        Location location = createAndSaveLocation();

        performGetByIdRequest(location.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(location.getName()))
                .andExpect(jsonPath("$.data.country").value(location.getCountry()));
    }

    @Test
    public void getById_whenInvalidId_shouldReturnNotFoundException() throws Exception {
        long invalidId = 999L;

        performGetByIdRequest(invalidId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.message").value(String.format(ExceptionMessages.LOCATION_NOT_FOUND, invalidId)));
    }

    private Location createAndSaveLocation() {
        return TestLocationDataHelper.createAndSaveLocation(locationRepository);
    }

    private void createAndSaveLocationWhenLocationCode(String locationCode) {
        TestLocationDataHelper.createAndSaveLocationWhenLocationCode(locationRepository, locationCode);
    }

    private ResultActions performSaveRequest(LocationRequestDto requestDto) throws Exception {
        return mockMvc.perform(post(EndpointPaths.LOCATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
    }

    private ResultActions performUpdateRequest(long id, LocationRequestDto requestDto) throws Exception {
        return mockMvc.perform(put(EndpointPaths.LOCATIONS + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));
    }

    private ResultActions performDeleteRequest(long id) throws Exception {
        return mockMvc.perform(delete(EndpointPaths.LOCATIONS + "/" + id)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performGetByIdRequest(long id) throws Exception {
        return mockMvc.perform(get(EndpointPaths.LOCATIONS + "/" + id)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performGetRequest() throws Exception {
        return mockMvc.perform(get(EndpointPaths.LOCATIONS)
                .contentType(MediaType.APPLICATION_JSON));
    }
}
