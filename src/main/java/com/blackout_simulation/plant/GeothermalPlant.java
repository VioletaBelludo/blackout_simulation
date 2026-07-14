package com.blackout_simulation.plant;

import edu.uoc.uoctron.model.location.LocationException;

import java.time.Duration;
import java.time.LocalTime;

public class GeothermalPlant extends RenewablePlant {

    private static final LocalTime AVAILABLE_FROM_TIME = LocalTime.of(0, 0, 0);
    private static final LocalTime AVAILABLE_TO_TIME = LocalTime.of(23, 59, 59);
    private static final Duration RESTART_DURATION = Duration.ofHours(1);
    private static final double STABILITY = 0.7;
    private static final String ICON = "geothermal.png";
    private static final String TYPE = "Geothermal";

    public GeothermalPlant(String name, double latitude, double longitude, String city, double maxCapacityMW, double efficiency) throws PowerPlantException, LocationException {
        super(name, ICON, latitude, longitude, city, AVAILABLE_FROM_TIME, AVAILABLE_TO_TIME, RESTART_DURATION, maxCapacityMW, STABILITY, efficiency);
    }

    public String getType() {
        return TYPE;
    }

}
