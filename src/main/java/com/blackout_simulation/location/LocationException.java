package com.blackout_simulation.location;

public class LocationException extends Exception {

    public static final String INVALID_LATITUDE = "Invalid latitude value. Latitude must be between -90 and 90 degrees.";
    public static final String INVALID_LONGITUDE = "Invalid longitude value. Longitude must be between -180 and 180 degrees.";
    public static final String INVALID_CITY = "City cannot be null or empty.";

    public LocationException(String message) {
        super(message);
    }
}
