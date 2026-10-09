package edu.spu.se411.lab07.bookings;

import java.time.LocalDate;

import edu.spu.se411.lab07.exceptions.InvalidArgumentException;
import edu.spu.se411.lab07.exceptions.MissingInformationException;

/**
 * Data shared by every booking. Each subclass adds its own data and its own
 * way of computing the total price.
 */
public abstract class Booking {

    private final String bookingId;
    private final String customerName;
    private final LocalDate travelDate;
    private final String destination;

    protected Booking(String bookingId, String customerName, LocalDate travelDate, String destination) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.travelDate = travelDate;
        this.destination = destination;
    }

    /** Each booking type has its own pricing rule. */
    public abstract double computeTotalPrice() throws MissingInformationException;

    /** Throws if a value that is entered later was never provided. */
    protected static <T> T require(T value, String field) throws MissingInformationException {
        if (value == null) {
            throw new MissingInformationException(field + " was not provided");
        }
        return value;
    }

    /** Throws if a value is outside [min, max]. */
    protected static void checkRange(double value, double min, double max, String field)
            throws InvalidArgumentException {
        if (value < min || value > max) {
            throw new InvalidArgumentException(
                    String.format("%s must be between %s and %s, got %s", field, fmt(min), fmt(max), fmt(value)));
        }
    }

    private static String fmt(double d) {
        return d == Math.rint(d) ? String.valueOf((long) d) : String.valueOf(d);
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s, %s to %s)", getClass().getSimpleName(), bookingId,
                customerName, travelDate, destination);
    }
}
