package com.blackout_simulation.plant;

import edu.uoc.uoctron.model.location.LocationException;

import java.time.Duration;
import java.time.LocalTime;

public abstract class ThermalPlant extends PowerPlant {

    ThermalFuelType fuelType;

    public ThermalPlant(String name, String icon, double latitude, double longitude, String city,
                        LocalTime availableFromTime, LocalTime availableToTime,
                        Duration restartTime, double maxCapacityMW, double stability,
                        ThermalFuelType fuelType) throws PowerPlantException, LocationException {
        super(name, icon, latitude, longitude, city, availableFromTime, availableToTime,
                restartTime, maxCapacityMW, stability);
        setFuelType(fuelType);
    }

    public ThermalFuelType getFuelType() {
        return fuelType;
    }

    private void setFuelType(ThermalFuelType fuelType) throws PowerPlantException {
        if (fuelType == null) {
            throw new PowerPlantException(PowerPlantException.INVALID_FUEL_TYPE);
        }
        this.fuelType = fuelType;
    }

}
