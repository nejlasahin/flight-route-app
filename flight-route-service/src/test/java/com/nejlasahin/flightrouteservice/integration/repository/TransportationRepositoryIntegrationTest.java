package com.nejlasahin.flightrouteservice.integration.repository;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TransportationRepositoryIntegrationTest {

    @Autowired
    private TransportationRepository transportationRepository;

    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    public void setUp() {
        transportationRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    public void findAllByOperatingDay_whenExistingDay_shouldReturnMatchingTransportation() {
        saveTransportation();
        List<Transportation> actual = transportationRepository.findAllByOperatingDay(1);
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    public void findAllByOperatingDay_whenNonExistingDay_shouldReturnEmptyList() {
        List<Transportation> actual = transportationRepository.findAllByOperatingDay(6);
        assertThat(actual.size()).isEqualTo(0);
    }

    @Test
    public void existsByOriginLocationAndDestinationLocationAndTransportationType_whenExistingTransportation_shouldReturnTrue() {
        Transportation savedTransportation = saveTransportation();

        Location originLocation = savedTransportation.getOriginLocation();
        Location destinationLocation = savedTransportation.getDestinationLocation();
        TransportationType type = savedTransportation.getTransportationType();

        boolean actual = transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationType(originLocation, destinationLocation, type);
        assertThat(actual).isTrue();
    }

    @Test
    public void existsByOriginLocationAndDestinationLocationAndTransportationType_whenNonExistingTransportation_shouldReturnFalse() {
        Transportation savedTransportation = saveTransportation();

        Location originLocation = savedTransportation.getOriginLocation();
        Location destinationLocation = savedTransportation.getDestinationLocation();
        TransportationType type = TransportationType.UBER;

        boolean actual = transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationType(originLocation, destinationLocation, type);
        assertThat(actual).isFalse();
    }

    @Test
    public void existsByOriginLocationAndDestinationLocationAndTransportationTypeAndIdNot_whenExistingTransportationOtherThanExcludedId_shouldReturnTrue() {
        Transportation savedTransportation = saveTransportation();

        Location originLocation = savedTransportation.getOriginLocation();
        Location destinationLocation = savedTransportation.getDestinationLocation();
        TransportationType type = savedTransportation.getTransportationType();

        boolean actual = transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationTypeAndIdNot(originLocation, destinationLocation, type, savedTransportation.getId() + 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void existsByOriginLocationAndDestinationLocationAndTransportationTypeAndIdNot_whenNonExistingTransportationOtherThanExcludedId_shouldReturnFalse() {
        Transportation savedTransportation = saveTransportation();

        Location originLocation = savedTransportation.getOriginLocation();
        Location destinationLocation = savedTransportation.getDestinationLocation();
        TransportationType type = savedTransportation.getTransportationType();

        boolean actual = transportationRepository.existsByOriginLocationAndDestinationLocationAndTransportationTypeAndIdNot(originLocation, destinationLocation, type, savedTransportation.getId());
        assertThat(actual).isFalse();
    }

    private Transportation saveTransportation() {
        Location origin = TestLocationDataHelper.createAndSaveLocation(locationRepository);
        Location destination = TestLocationDataHelper.createAndSaveLocation(locationRepository);

        return TestTransportationDataHelper.createAndSaveTransportation(origin, destination, TransportationType.FLIGHT, 7, transportationRepository);
    }
}
