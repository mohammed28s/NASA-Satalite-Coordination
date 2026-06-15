package com.Satliate.NASA.Expection;







public class SpaceTrackException extends RuntimeException {

    private final int statusCode;

    public SpaceTrackException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
