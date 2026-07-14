package com.blackout_simulation.model.plant;

import edu.uoc.uoctron.model.location.LocationException;

import java.time.Duration;
import java.time.LocalTime;

public class WindPlant extends RenewablePlant {

    private static final LocalTime AVAILABLE_FROM_TIME = LocalTime.of(0, 0, 0);
    private static final LocalTime AVAILABLE_TO_TIME = LocalTime.of(23, 59, 59);
    private static final Duration RESTART_DURATION = Duration.ofMinutes(6);
    private static final double STABILITY = 0.2;
    private static final String ICON = "wind.png";
    private static final String TYPE = "Wind";

    public WindPlant(String name, double latitude, double longitude, String city, double maxCapacityMW, double efficiency) throws PowerPlantException, LocationException {
        super(name, ICON, latitude, longitude, city, AVAILABLE_FROM_TIME, AVAILABLE_TO_TIME, RESTART_DURATION, maxCapacityMW, STABILITY, efficiency);
    }

    public String getType() {
        return TYPE;
    }

}
