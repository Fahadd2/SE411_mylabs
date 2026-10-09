package edu.spu.se411.lab07.bookings;

import java.time.LocalDate;

import edu.spu.se411.lab07.config.Config;
import edu.spu.se411.lab07.exceptions.InvalidArgumentException;
import edu.spu.se411.lab07.exceptions.MissingInformationException;

public class TrainBooking extends Booking {

    private final SeatClass seatClass;
    private Double distanceKm; // null until the system enters it

    public TrainBooking(String bookingId, String customerName, LocalDate travelDate, String destination,
            SeatClass seatClass) {
        super(bookingId, customerName, travelDate, destination);
        this.seatClass = seatClass;
    }

    public void setDistanceKm(double distanceKm) throws InvalidArgumentException {
        checkRange(distanceKm, Config.MIN_TRAIN_DISTANCE, Config.MAX_TRAIN_DISTANCE, "Distance (km)");
        this.distanceKm = distanceKm;
    }

    @Override
    public double computeTotalPrice() throws MissingInformationException {
        double distance = require(distanceKm, "Distance");
        return distance * seatClass.getRatePerKm();
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }
}
