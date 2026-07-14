package com.blackout_simulation.plant;

import edu.uoc.uoctron.model.location.LocationException;

import java.time.Duration;
import java.time.LocalTime;

public abstract class RenewablePlant extends PowerPlant {

    private double efficiency;

    public RenewablePlant(String name, String icon, double latitude, double longitude, String city,
                          LocalTime availableFromTime, LocalTime availableToTime,
                          Duration restartTime, double maxCapacityMW, double stability,
                          double efficiency) throws PowerPlantException, LocationException {
        super(name, icon, latitude, longitude, city, availableFromTime, availableToTime,
                restartTime, maxCapacityMW, stability);
        setEfficiency(efficiency);
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) throws PowerPlantException {
        if (efficiency < 0.0 || efficiency > 1.0) {
            throw new PowerPlantException(PowerPlantException.INVALID_EFFICIENCY);
        }
        this.efficiency = efficiency;
    }

}
