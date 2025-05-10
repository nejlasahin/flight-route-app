package com.nejlasahin.flightrouteservice.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {
    private T data;

    @JsonProperty("response_date")
    private Date responseDate;

    private boolean success;

    private String message;

    public RestResponse(T data, boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
        responseDate = new Date();
    }

    public static <T> RestResponse<T> of(T t, String message) {
        return new RestResponse<>(t, true, message);
    }

    public static <T> RestResponse<T> error(T t, String message) {
        return new RestResponse<>(t, false, message);
    }

    public static <T> RestResponse<T> empty(String message) {
        return new RestResponse<>(null, true, message);
    }
}