package com.nejlasahin.flightrouteservice.repository;

import com.nejlasahin.flightrouteservice.entity.Location;
import com.nejlasahin.flightrouteservice.entity.Transportation;
import com.nejlasahin.flightrouteservice.enumeration.TransportationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Long> {
    @Query("SELECT t FROM Transportation t JOIN t.operatingDays d WHERE d.operatingDay = :operatingDay")
    List<Transportation> findAllByOperatingDay(@Param("operatingDay") Integer operatingDay);

    boolean existsByOriginLocationAndDestinationLocationAndTransportationType(Location originLocation, Location destinationLocation, TransportationType type);

    boolean existsByOriginLocationAndDestinationLocationAndTransportationTypeAndIdNot(Location originLocation, Location destinationLocation, TransportationType type, Long id);
}