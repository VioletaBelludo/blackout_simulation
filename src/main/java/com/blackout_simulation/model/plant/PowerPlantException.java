package com.blackout_simulation.model.plant;

public class PowerPlantException extends Exception {

    public static final String INVALID_NAME = "Invalid name. Name cannot be null or empty.";
    public static final String INVALID_ICON = "Invalid icon. Icon cannot be null and must end with .png.";
    public static final String INVALID_AVAILABLE_FROM_TIME = "Invalid available from time. Time cannot be null.";
    public static final String INVALID_AVAILABLE_TO_TIME = "Invalid available to time. Time cannot be null.";
    public static final String INVALID_RESTART_DURATION = "Invalid restart time. Time cannot be null or negative.";
    public static final String INVALID_MAX_CAPACITY = "Invalid max capacity. Capacity cannot be negative.";
    public static final String INVALID_STABILITY = "Invalid stability. Stability has to be between 0.0 and 1.0.";
    public static final String INVALID_FUEL_TYPE = "Invalid fuel type. Fuel type cannot be null.";
    public static final String INVALID_EFFICIENCY = "Invalid efficiency. Efficiency has to be between 0.0 and 1.0.";
    public static final String INVALID_STATE = "Invalid state. State cannot be null.";

    public PowerPlantException(String message) {
        super(message);
    }

}
