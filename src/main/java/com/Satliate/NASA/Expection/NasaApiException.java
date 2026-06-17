package com.Satliate.NASA.Expection;

import lombok.Getter;

@Getter
public class NasaApiException extends RuntimeException {
    private final int statusCode;

    public NasaApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
