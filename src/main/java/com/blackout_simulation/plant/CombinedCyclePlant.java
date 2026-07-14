package com.blackout_simulation.plant;

import edu.uoc.uoctron.model.location.LocationException;

import java.time.Duration;
import java.time.LocalTime;

public class CombinedCyclePlant extends ThermalPlant {

    private static final LocalTime AVAILABLE_FROM_TIME = LocalTime.of(0, 0, 0);
    private static final LocalTime AVAILABLE_TO_TIME = LocalTime.of(23, 59, 59);
    private static final Duration RESTART_DURATION = Duration.ofHours(2);
    private static final double STABILITY = 0.7;
    private static final String ICON = "combined_cycle.png";
    private static final String TYPE = "Combined cycle";

    public CombinedCyclePlant(String name, double latitude, double longitude, String city, double maxCapacityMW) throws PowerPlantException, LocationException {
        super(name, ICON, latitude, longitude, city, AVAILABLE_FROM_TIME, AVAILABLE_TO_TIME, RESTART_DURATION, maxCapacityMW, STABILITY, ThermalFuelType.NATURAL_GAS);
    }

    public String getType() {
        return TYPE;
    }

}
