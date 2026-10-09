package edu.spu.se411.lab07.bookings;

import java.time.LocalDate;

import edu.spu.se411.lab07.config.Config;
import edu.spu.se411.lab07.exceptions.InvalidArgumentException;
import edu.spu.se411.lab07.exceptions.MissingInformationException;

public class CarRentalBooking extends Booking {

    private final double dailyRate;
    private Integer days; // null until the customer enters it

    public CarRentalBooking(String bookingId, String customerName, LocalDate travelDate, String destination,
            double dailyRate) {
        super(bookingId, customerName, travelDate, destination);
        this.dailyRate = dailyRate;
    }

    public void setDays(int days) throws InvalidArgumentException {
        checkRange(days, Config.MIN_RENTAL_DAYS, Config.MAX_RENTAL_DAYS, "Rental days");
        this.days = days;
    }

    @Override
    public double computeTotalPrice() throws MissingInformationException {
        int rentalDays = require(days, "Rental days");
        return dailyRate * rentalDays;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public Integer getDays() {
        return days;
    }
}
