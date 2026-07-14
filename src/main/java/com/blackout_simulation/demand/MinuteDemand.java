package com.blackout_simulation.demand;

import java.time.LocalTime;

public class MinuteDemand {
    private LocalTime time;
    private double megawatts;

    public MinuteDemand(LocalTime hour, double megawatts) throws MinuteDemandException {
        setHour(hour);
        setMegawatts(megawatts);
    }

    public LocalTime getTime() {
        return time;
    }

    private void setHour(LocalTime time) throws MinuteDemandException {
        if (time == null) {
            throw new MinuteDemandException(MinuteDemandException.INVALID_TIME);
        }
        this.time = time;
    }

    public double getMegawatts() {
        return megawatts;
    }

    private void setMegawatts(double megawatts) throws MinuteDemandException {
        if (megawatts < 0) {
            throw new MinuteDemandException(MinuteDemandException.INVALID_MEGAWATTS);
        }
        this.megawatts = megawatts;
    }
}
