package com.blackout_simulation.model.plant;

import edu.uoc.uoctron.model.location.Location;
import edu.uoc.uoctron.model.location.LocationException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class PowerPlant {

    private String name;
    private String icon;
    private Location location;
    private LocalTime availableFromTime;
    private LocalTime availableToTime;
    private Duration restartDuration;
    private double maxCapacityMW;
    private double stability;
    private LocalDateTime restartInitiationTime;
    private PlantState state;

    public PowerPlant(String name, String icon, double latitude, double longitude, String city, LocalTime availableFromTime, LocalTime availableToTime,
                      Duration restartDuration, double maxCapacityMW, double stability) throws PowerPlantException, LocationException {
        setName(name);
        setIcon(icon);
        setLocation(latitude, longitude, city);
        setAvailableFromTime(availableFromTime);
        setAvailableToTime(availableToTime);
        setRestartDuration(restartDuration);
        setMaxCapacityMW(maxCapacityMW);
        setStability(stability);
        setRestartInitiationTime(null);
        setState(PlantState.ONLINE);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) throws PowerPlantException {
        if (name == null || name.isEmpty()) {
            throw new PowerPlantException(PowerPlantException.INVALID_NAME);
        }
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    private void setIcon(String icon) throws PowerPlantException {
        if (icon == null || !icon.endsWith(".png")) {
            throw new PowerPlantException(PowerPlantException.INVALID_ICON);
        }
        this.icon = icon;
    }

    public Location getLocation() {
        return location;
    }

    private void setLocation(double latitude, double longitude, String city) throws LocationException {
        this.location = new Location(latitude, longitude, city);
    }

    public LocalTime getAvailableFromTime() {
        return availableFromTime;
    }

    private void setAvailableFromTime(LocalTime availableFromTime) throws PowerPlantException {
        if (availableFromTime == null) {
            throw new PowerPlantException(PowerPlantException.INVALID_AVAILABLE_FROM_TIME);
        }
        this.availableFromTime = availableFromTime;
    }

    public LocalTime getAvailableToTime() {
        return availableToTime;
    }

    private void setAvailableToTime(LocalTime availableToTime) throws PowerPlantException {
        if (availableToTime == null) {
            throw new PowerPlantException(PowerPlantException.INVALID_AVAILABLE_TO_TIME);
        }
        this.availableToTime = availableToTime;
    }

    public Duration getRestartDuration() {
        return restartDuration;
    }

    private void setRestartDuration(Duration restartDuration) throws PowerPlantException {
        if (restartDuration == null || restartDuration.isNegative()) {
            throw new PowerPlantException(PowerPlantException.INVALID_RESTART_DURATION);
        }
        this.restartDuration = restartDuration;
    }

    public double getMaxCapacityMW() {
        return maxCapacityMW;
    }

    private void setMaxCapacityMW(double maxCapacityMW) throws PowerPlantException {
        if (maxCapacityMW < 0) {
            throw new PowerPlantException(PowerPlantException.INVALID_MAX_CAPACITY);
        }
        this.maxCapacityMW = maxCapacityMW;
    }

    public double getStability() {
        return stability;
    }

    private void setStability(double stability) throws PowerPlantException {
        if (stability < 0.0 || stability > 1.0) {
            throw new PowerPlantException(PowerPlantException.INVALID_STABILITY);
        }
        this.stability = stability;
    }

    public abstract String getType();

    public LocalDateTime getRestartInitiationTime() {
        return restartInitiationTime;
    }

    public void setRestartInitiationTime(LocalDateTime restartInitiationTime) {
        this.restartInitiationTime = restartInitiationTime;
    }

    public PlantState getState() {
        return state;
    }

    public void setState(PlantState state) throws PowerPlantException {
        if (state == null) {
            throw new PowerPlantException(PowerPlantException.INVALID_STATE);
        }
        this.state = state;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("type", getType());

        JSONObject locationJson = new JSONObject(getLocation().toString());
        for (String key : locationJson.keySet()) {
            json.put(key, locationJson.get(key));
        }

        json.put("icon", getIcon());
        json.put("availableFrom ", getAvailableFromTime());
        json.put("availableTo", getAvailableToTime());
        json.put("restartTime", getRestartDuration());
        json.put("maxCapacityMW", getMaxCapacityMW());
        return json.toString();
    }

}
