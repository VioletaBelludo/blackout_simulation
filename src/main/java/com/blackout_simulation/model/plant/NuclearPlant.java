package com.blackout_simulation.model.plant;

import com.blackout_simulation.model.location.LocationException;

import java.time.Duration;
import java.time.LocalTime;

public class NuclearPlant extends PowerPlant {

    private static final LocalTime AVAILABLE_FROM_TIME = LocalTime.of(0, 0, 0);
    private static final LocalTime AVAILABLE_TO_TIME = LocalTime.of(23, 59, 59);
    private static final Duration RESTART_DURATION = Duration.ofHours(24);
    private static final double STABILITY = 1.0;
    private static final String ICON = "nuclear.png";
    private static final String TYPE = "Nuclear";

    public NuclearPlant(String name, double latitude, double longitude, String city, double maxCapacityMW) throws PowerPlantException, LocationException {
        super(name, ICON, latitude, longitude, city, AVAILABLE_FROM_TIME, AVAILABLE_TO_TIME, RESTART_DURATION, maxCapacityMW, STABILITY);
    }

    public String getType() {
        return TYPE;
    }

}
