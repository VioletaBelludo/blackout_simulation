package com.blackout_simulation.simulation;

import java.time.LocalDateTime;
import java.util.Map;

public class SimulationResult {

    private LocalDateTime time;
    private double generatedMW;
    private double expectedDemandMW;
    private double averageStability;
    private Map<String, Double> generatedByTypeMW;

    public SimulationResult(LocalDateTime time, double generatedMW, double expectedDemandMW,
                            double averageStability, Map<String, Double> generatedByTypeMW) {
        setTime(time);
        setGeneratedMW(generatedMW);
        setExpectedDemandMW(expectedDemandMW);
        setAverageStability(averageStability);
        setGeneratedByTypeMW(generatedByTypeMW);
    }

    public LocalDateTime getTime() {
        return time;
    }

    private void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getGeneratedMW() {
        return generatedMW;
    }

    private void setGeneratedMW(double generatedMW) {
        this.generatedMW = generatedMW;
    }

    public double getExpectedDemandMW() {
        return expectedDemandMW;
    }

    private void setExpectedDemandMW(double expectedDemandMW) {
        this.expectedDemandMW = expectedDemandMW;
    }

    public double getAverageStability() {
        return averageStability;
    }

    private void setAverageStability(double averageStability) {
        this.averageStability = averageStability;
    }

    public Map<String, Double> getGeneratedByTypeMW() {
        return generatedByTypeMW;
    }

    private void setGeneratedByTypeMW(Map<String, Double> generatedByTypeMW) {
        this.generatedByTypeMW = generatedByTypeMW;
    }

}
