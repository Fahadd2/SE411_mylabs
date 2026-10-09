package edu.spu.se411.lab07.bookings;

import edu.spu.se411.lab07.config.Config;

public enum SeatClass {
    STANDARD(Config.TRAIN_STANDARD_RATE),
    FIRST_CLASS(Config.TRAIN_FIRST_CLASS_RATE);

    private final double ratePerKm;

    SeatClass(double ratePerKm) {
        this.ratePerKm = ratePerKm;
    }

    public double getRatePerKm() {
        return ratePerKm;
    }
}
