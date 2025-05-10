package com.nejlasahin.flightrouteservice.integration.controller;

import com.nejlasahin.flightrouteservice.constants.EndpointPaths;
import com.nejlasahin.flightrouteservice.constants.ExceptionMessages;
import com.nejlasahin.flightrouteservice.constants.SuccessMessages;
import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.entity.Transportation;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import com.nejlasahin.flightrouteservice.helper.TestLocationDataHelper;
import com.nejlasahin.flightrouteservice.helper.TestTransportationDataHelper;
import com.nejlasahin.flightrouteservice.repository.LocationRepository;
import com.nejlasahin.flightrouteservice.repository.TransportationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RouteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TransportationRepository transportationRepository;

    private LocalDate travelDate;
    private int dayOfWeek;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        transportationRepository.deleteAll();
        travelDate = LocalDate.now().plusDays(1);
        dayOfWeek = travelDate.getDayOfWeek().getValue();
    }

    @Test
    public void getAllByDate_WhenValidParams_ShouldReturnDirectFlightRoute() throws Exception {
        Location origin = createLocation();
        Location destination = createLocation();
        Transportation flight = createTransportation(origin, destination, TransportationType.FLIGHT);

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(String.format(SuccessMessages.ROUTES_RETRIEVED_WITH_TRAVEL_DATE, travelDate)));

        assertTransportations(List.of(flight));
    }

    @Test
    public void getAllByDate_WhenValidParams_ShouldReturnRoutesListWithPreFlightTransfer() throws Exception {
        Location origin = createLocation();
        Location transfer = createLocation();
        Location destination = createLocation();

        Transportation bus = createTransportation(origin, transfer, TransportationType.BUS);
        Transportation flight = createTransportation(transfer, destination, TransportationType.FLIGHT);

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(String.format(SuccessMessages.ROUTES_RETRIEVED_WITH_TRAVEL_DATE, travelDate)));

        assertTransportations(List.of(bus, flight));
    }

    @Test
    public void getAllByDate_WhenValidParams_ShouldReturnRoutesListWithPostFlightTransfer() throws Exception {
        Location origin = createLocation();
        Location transfer = createLocation();
        Location destination = createLocation();

        Transportation flight = createTransportation(origin, transfer, TransportationType.FLIGHT);
        Transportation uber = createTransportation(transfer, destination, TransportationType.UBER);

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(String.format(SuccessMessages.ROUTES_RETRIEVED_WITH_TRAVEL_DATE, travelDate)));

        assertTransportations(List.of(flight, uber));
    }

    @Test
    public void getAllByDate_WhenValidParams_ShouldReturnRoutesListWithPreAndPostFlightTransfer() throws Exception {
        Location origin = createLocation();
        Location firstTransfer = createLocation();
        Location secondTransfer = createLocation();
        Location destination = createLocation();

        Transportation subway = createTransportation(origin, firstTransfer, TransportationType.SUBWAY);
        Transportation flight = createTransportation(firstTransfer, secondTransfer, TransportationType.FLIGHT);
        Transportation uber = createTransportation(secondTransfer, destination, TransportationType.UBER);

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(String.format(SuccessMessages.ROUTES_RETRIEVED_WITH_TRAVEL_DATE, travelDate)));

        assertTransportations(Arrays.asList(subway, flight, uber));
    }

    @Test
    public void getAllByDate_WhenValidParams_ShouldReturnRoutesEmptyList() throws Exception {
        Location origin = createLocation();
        Location destination = createLocation();

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value(String.format(SuccessMessages.ROUTES_RETRIEVED_WITH_TRAVEL_DATE, travelDate)));
    }

    @Test
    public void getAllByDate_WhenLocationDoesNotExist_ShouldReturnNotFoundException() throws Exception {
        long invalidId = 999L;

        performGetRequest(invalidId, invalidId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(ExceptionMessages.LOCATION_NOT_FOUND, invalidId)));
    }

    @Test
    public void getAllByDate_WhenTravelDateIsInPast_ShouldReturnParameterValidationException() throws Exception {
        Location origin = createLocation();
        Location destination = createLocation();
        String pastDate = LocalDate.now().minusDays(1).toString();

        mockMvc.perform(get(EndpointPaths.ROUTES)
                        .param("origin_location_id", String.valueOf(origin.getId()))
                        .param("destination_location_id", String.valueOf(destination.getId()))
                        .param("travel_date", pastDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.TRAVEL_DATE_IN_PAST));
    }

    @Test
    public void getAllByDate_WhenTravelDateIsNull_ShouldReturnMethodArgumentTypeMismatchException() throws Exception {
        performGetRequest(null, null)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnSameLocationException() throws Exception {
        Location location = createLocation();

        performGetRequest(location.getId(), location.getId())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.SAME_LOCATION_EXCEPTION));
    }

    @Test
    public void getAllByDate_WhenMultipleTransfersBeforeFlight_ShouldReturnEmptyList() throws Exception {
        Location origin = createLocation();
        Location firstTransfer = createLocation();
        Location secondTransfer = createLocation();
        Location destination = createLocation();

        createTransportation(origin, firstTransfer, TransportationType.UBER);
        createTransportation(firstTransfer, secondTransfer, TransportationType.BUS);
        createTransportation(secondTransfer, destination, TransportationType.FLIGHT);

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    public void getAllByDate_WhenRouteWithoutFlight_ShouldReturnEmptyList() throws Exception {
        Location origin = createLocation();
        Location transfer = createLocation();
        Location destination = createLocation();

        createTransportation(origin, transfer, TransportationType.UBER);
        createTransportation(transfer, destination, TransportationType.BUS);

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    public void getAllByDate_WhenMultipleFlightsInRoute_ShouldReturnEmptyList() throws Exception {
        Location origin = createLocation();
        Location transfer = createLocation();
        Location destination = createLocation();

        createTransportation(origin, transfer, TransportationType.FLIGHT);
        createTransportation(transfer, destination, TransportationType.FLIGHT);

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    public void getAllByDate_WhenMultipleTransfersAfterFlight_ShouldReturnEmptyList() throws Exception {
        Location origin = createLocation();
        Location firstTransfer = createLocation();
        Location secondTransfer = createLocation();
        Location destination = createLocation();

        createTransportation(origin, firstTransfer, TransportationType.FLIGHT);
        createTransportation(firstTransfer, secondTransfer, TransportationType.SUBWAY);
        createTransportation(secondTransfer, destination, TransportationType.UBER);

        performGetRequest(origin.getId(), destination.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    private Location createLocation() {
        return TestLocationDataHelper.createAndSaveLocation(locationRepository);
    }

    private Transportation createTransportation(Location origin, Location destination, TransportationType type) {
        return TestTransportationDataHelper.createAndSaveTransportation(origin, destination, type, dayOfWeek, transportationRepository);
    }

    private ResultActions performGetRequest(Long originId, Long destinationId) throws Exception {
        return mockMvc.perform(get(EndpointPaths.ROUTES)
                .param("origin_location_id", String.valueOf(originId))
                .param("destination_location_id", String.valueOf(destinationId))
                .param("travel_date", String.valueOf(travelDate))
                .contentType(MediaType.APPLICATION_JSON));
    }

    private void assertTransportations(List<Transportation> transportations) throws Exception {
        for (int i = 0; i < transportations.size(); i++) {
            mockMvc.perform(get(EndpointPaths.ROUTES)
                            .param("origin_location_id", String.valueOf(transportations.get(0).getOriginLocation().getId()))
                            .param("destination_location_id", String.valueOf(transportations.get(transportations.size() - 1).getDestinationLocation().getId()))
                            .param("travel_date", String.valueOf(travelDate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].transportations[" + i + "].origin_location.id").value(transportations.get(i).getOriginLocation().getId()))
                    .andExpect(jsonPath("$.data[0].transportations[" + i + "].destination_location.id").value(transportations.get(i).getDestinationLocation().getId()))
                    .andExpect(jsonPath("$.data[0].transportations[" + i + "].transportation_type").value(transportations.get(i).getTransportationType().name()));
        }
    }
}
