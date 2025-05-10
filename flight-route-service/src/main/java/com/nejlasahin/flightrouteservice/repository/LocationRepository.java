package com.nejlasahin.flightrouteservice.repository;

import com.nejlasahin.flightrouteservice.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l.locationCode FROM Location l WHERE l.id = :id")
    String findLocationCodeById(Long id);

    boolean existsByLocationCode(String locationCode);

    boolean existsByLocationCodeAndIdNot(String locationCode, Long id);
}