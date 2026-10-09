package edu.spu.se411.lab07.bookings;

import java.time.LocalDate;

import edu.spu.se411.lab07.config.Config;
import edu.spu.se411.lab07.exceptions.InvalidArgumentException;
import edu.spu.se411.lab07.exceptions.MissingInformationException;

public class FlightBooking extends Booking {

    private final double basePrice;
    private Double luggageWeight; // null until the customer enters it

    public FlightBooking(String bookingId, String customerName, LocalDate travelDate, String destination,
            double basePrice) {
        super(bookingId, customerName, travelDate, destination);
        this.basePrice = basePrice;
    }

    public void setLuggageWeight(double luggageWeight) throws InvalidArgumentException {
        checkRange(luggageWeight, Config.MIN_LUGGAGE_WEIGHT, Config.MAX_LUGGAGE_WEIGHT, "Luggage weight (kg)");
        this.luggageWeight = luggageWeight;
    }

    @Override
    public double computeTotalPrice() throws MissingInformationException {
        double weight = require(luggageWeight, "Luggage weight");
        return basePrice + weight * Config.EXTRA_LUGGAGE_RATE;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public Double getLuggageWeight() {
        return luggageWeight;
    }
}
