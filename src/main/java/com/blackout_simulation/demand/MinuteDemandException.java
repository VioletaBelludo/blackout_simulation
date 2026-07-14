package com.blackout_simulation.demand;

public class MinuteDemandException extends Exception {

    public static final String INVALID_TIME = "Invalid time demand. Time demand cannot be null.";
    public static final String INVALID_MEGAWATTS = "Invalid megawatts. Megawatts cannot be negative.";

    public MinuteDemandException(String message) {
        super(message);
    }

}
