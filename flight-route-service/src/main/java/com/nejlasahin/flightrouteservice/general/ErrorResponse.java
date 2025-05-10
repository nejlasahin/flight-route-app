package com.nejlasahin.flightrouteservice.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;

    private String path;

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("response_date")
    private Date responseDate = new Date();
}