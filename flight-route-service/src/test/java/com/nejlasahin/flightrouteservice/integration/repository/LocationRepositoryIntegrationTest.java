package com.nejlasahin.flightrouteservice.integration.repository;

import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.helper.TestLocationDataHelper;
import com.nejlasahin.flightrouteservice.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LocationRepositoryIntegrationTest {

    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
    }

    @Test
    public void existsByLocationCode_whenValidLocationCode_shouldReturnTrue() {
        Location location = createAndSaveLocation();
        boolean actual = locationRepository.existsByLocationCode(location.getLocationCode());
        assertThat(actual).isTrue();
    }

    @Test
    public void existsByLocationCode_whenInvalidLocationCode_shouldReturnFalse() {
        boolean actual = locationRepository.existsByLocationCode("XYZ");
        assertThat(actual).isFalse();
    }

    @Test
    public void existsByLocationCodeAndIdNot_whenValidLocationCode_shouldReturnTrue() {
        Location location = createAndSaveLocation();
        boolean actual = locationRepository.existsByLocationCodeAndIdNot(location.getLocationCode(), 999L);
        assertThat(actual).isTrue();
    }

    @Test
    public void existsByLocationCodeAndIdNot_whenInvalidLocationCode_shouldReturnFalse() {
        boolean actual = locationRepository.existsByLocationCodeAndIdNot("XYZ", 1L);
        assertThat(actual).isFalse();
    }

    @Test
    public void findLocationCodeById_whenExistingId_shouldReturnLocationCode() {
        Location location = createAndSaveLocation();
        String actual = locationRepository.findLocationCodeById(location.getId());
        assertThat(actual).isEqualTo(location.getLocationCode());
    }

    @Test
    public void findLocationCodeById_whenNonExistingId_shouldReturnNull() {
        String actual = locationRepository.findLocationCodeById(999L);
        assertThat(actual).isNull();
    }

    private Location createAndSaveLocation() {
        return TestLocationDataHelper.createAndSaveLocation(locationRepository);
    }
}
