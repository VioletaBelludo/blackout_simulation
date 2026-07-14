package com.blackout_simulation.plant;

public enum ThermalFuelType {
    NATURAL_GAS("Natural Gas"),
    COAL("Coal"),
    FUEL_OIL("Fuel Oil"),
    BIOMASS("Biomass");

    private final String description;

    ThermalFuelType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
