package com.blackout_simulation.location;

import org.json.JSONObject;

public class Location {

    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LONGITUDE = 180.0;
    private static final double MIN_LONGITUDE = -180.0;

    private double latitude;
    private double longitude;
    private String city;

    public Location(double latitude, double longitude, String city) throws LocationException {
        setLatitude(latitude);
        setLongitude(longitude);
        setCity(city);
    }

    public double getLatitude() {
        return latitude;
    }

    private void setLatitude(double latitude) throws LocationException {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new LocationException(LocationException.INVALID_LATITUDE);
        }
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private void setLongitude(double longitude) throws LocationException {
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new LocationException(LocationException.INVALID_LONGITUDE);
        }
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    private void setCity(String city) throws LocationException {
        if (city == null || city.isEmpty()) {
            throw new LocationException(LocationException.INVALID_CITY);
        }
        this.city = city;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("city", city);
        json.put("latitude", latitude);
        json.put("longitude", longitude);
        return json.toString();
    }


}
