package com.blackout_simulation.model.plant;

import com.blackout_simulation.model.location.LocationException;

import java.time.Duration;
import java.time.LocalTime;

public class HydroPlant extends RenewablePlant {

    private static final LocalTime AVAILABLE_FROM_TIME = LocalTime.of(0, 0, 0);
    private static final LocalTime AVAILABLE_TO_TIME = LocalTime.of(23, 59, 59);
    private static final Duration RESTART_DURATION = Duration.ofMinutes(3);
    private static final double STABILITY = 0.8;
    private static final String ICON = "hydro.png";
    private static final String TYPE = "Hydroelectric";

    public HydroPlant(String name, double latitude, double longitude, String city, double maxCapacityMW, double efficiency) throws PowerPlantException, LocationException {
        super(name, ICON, latitude, longitude, city, AVAILABLE_FROM_TIME, AVAILABLE_TO_TIME, RESTART_DURATION, maxCapacityMW, STABILITY, efficiency);
    }

    public String getType() {
        return TYPE;
    }

}
