package com.nejlasahin.flightrouteservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nejlasahin.flightrouteservice.constants.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequestDto {
    @NotBlank(message = ValidationMessages.NAME_NOT_BLANK)
    @Size(max = 100, message = ValidationMessages.NAME_SIZE)
    private String name;

    @NotBlank(message = ValidationMessages.COUNTRY_NOT_BLANK)
    @Size(max = 100, message = ValidationMessages.COUNTRY_SIZE)
    private String country;

    @NotBlank(message = ValidationMessages.CITY_NOT_BLANK)
    @Size(max = 100, message = ValidationMessages.CITY_SIZE)
    private String city;

    @NotBlank(message = ValidationMessages.LOCATION_CODE_NOT_BLANK)
    @Size(min = 3, max = 50, message = ValidationMessages.LOCATION_CODE_SIZE)
    @JsonProperty("location_code")
    private String locationCode;
}