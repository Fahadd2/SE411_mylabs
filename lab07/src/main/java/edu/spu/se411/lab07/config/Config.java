package edu.spu.se411.lab07.config;

/** Global configuration: rates and limits shared by all bookings. */
public final class Config {

    // Flight
    public static final double EXTRA_LUGGAGE_RATE = 15.0;      // per kg
    public static final double MIN_LUGGAGE_WEIGHT = 0;
    public static final double MAX_LUGGAGE_WEIGHT = 40;

    // Train
    public static final double TRAIN_STANDARD_RATE = 0.40;     // per km
    public static final double TRAIN_FIRST_CLASS_RATE = 0.75;  // per km
    public static final double MIN_TRAIN_DISTANCE = 1;
    public static final double MAX_TRAIN_DISTANCE = 2000;

    // Car rental
    public static final int MIN_RENTAL_DAYS = 1;
    public static final int MAX_RENTAL_DAYS = 30;

    private Config() {
        // constants only, no instances
    }
}
