package com.nejlasahin.flightrouteservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nejlasahin.flightrouteservice.constants.ValidationMessages;
import com.nejlasahin.flightrouteservice.validator.ValidOperatingDays;
import com.nejlasahin.flightrouteservice.validator.ValidTransportationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportationRequestDto {
    @NotNull(message = ValidationMessages.ORIGIN_LOCATION_ID_NOT_NULL)
    @Min(value = 1, message = ValidationMessages.ORIGIN_LOCATION_ID_POSITIVE)
    @JsonProperty("origin_location_id")
    private Long originLocationId;

    @NotNull(message = ValidationMessages.DESTINATION_LOCATION_ID_NOT_NULL)
    @Min(value = 1, message = ValidationMessages.DESTINATION_LOCATION_ID_POSITIVE)
    @JsonProperty("destination_location_id")
    private Long destinationLocationId;

    @NotNull(message = ValidationMessages.TRANSPORTATION_TYPE_NOT_NULL)
    @ValidTransportationType
    @JsonProperty("transportation_type")
    private String transportationType;

    @NotEmpty(message = ValidationMessages.OPERATING_DAYS_NOT_EMPTY)
    @Size(min = 1, message = ValidationMessages.OPERATING_DAYS_MIN)
    @ValidOperatingDays
    @JsonProperty("operating_days")
    private List<Integer> operatingDays;
}