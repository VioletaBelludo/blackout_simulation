package com.blackout_simulation.model.plant;

import edu.uoc.uoctron.model.location.LocationException;

import java.time.Duration;
import java.time.LocalTime;

public class CoalPlant extends ThermalPlant {

    private static final LocalTime AVAILABLE_FROM_TIME = LocalTime.of(0, 0, 0);
    private static final LocalTime AVAILABLE_TO_TIME = LocalTime.of(23, 59, 59);
    private static final Duration RESTART_DURATION = Duration.ofHours(8);
    private static final double STABILITY = 0.9;
    private static final String ICON = "coal.png";
    private static final String TYPE = "Coal";

    public CoalPlant(String name, double latitude, double longitude, String city, double maxCapacityMW) throws PowerPlantException, LocationException {
        super(name, ICON, latitude, longitude, city, AVAILABLE_FROM_TIME, AVAILABLE_TO_TIME, RESTART_DURATION, maxCapacityMW, STABILITY, ThermalFuelType.COAL);
    }

    public String getType() {
        return TYPE;
    }

}
