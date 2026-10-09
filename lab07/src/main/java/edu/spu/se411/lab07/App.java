package edu.spu.se411.lab07;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.spu.se411.lab07.bookings.Booking;
import edu.spu.se411.lab07.bookings.CarRentalBooking;
import edu.spu.se411.lab07.bookings.FlightBooking;
import edu.spu.se411.lab07.bookings.SeatClass;
import edu.spu.se411.lab07.bookings.TrainBooking;
import edu.spu.se411.lab07.exceptions.BookingException;
import edu.spu.se411.lab07.exceptions.InvalidArgumentException;
import edu.spu.se411.lab07.exceptions.MissingInformationException;

public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    /** Works for any booking type: the right pricing rule is picked at runtime. */
    public static double computeTotalPrice(Booking booking) throws MissingInformationException {
        return booking.computeTotalPrice();
    }

    public static void main(String[] args) {
        logger.info("Application is starting...");
        try {
            validBookings();
            invalidBookings();
        } finally {
            logger.info("Application is stopping.");
        }
    }

    private static void validBookings() {
        System.out.println("=== Valid bookings ===");
        LocalDate date = LocalDate.of(2026, 11, 15);
        try {
            FlightBooking flight = new FlightBooking("F-001", "Fahad Alharbi", date, "Jeddah", 450.0);
            flight.setLuggageWeight(23);

            TrainBooking standard = new TrainBooking("T-001", "Sara Ali", date, "Dammam", SeatClass.STANDARD);
            standard.setDistanceKm(400);

            TrainBooking firstClass = new TrainBooking("T-002", "Omar Saleh", date, "Dammam", SeatClass.FIRST_CLASS);
            firstClass.setDistanceKm(400);

            CarRentalBooking car = new CarRentalBooking("C-001", "Noura Khalid", date, "Abha", 180.0);
            car.setDays(5);

            List<Booking> bookings = List.of(flight, standard, firstClass, car);
            double grandTotal = 0;
            for (Booking booking : bookings) {
                double price = computeTotalPrice(booking);
                grandTotal += price;
                System.out.printf("%-60s %10.2f SAR%n", booking, price);
            }
            System.out.printf("%-60s %10.2f SAR%n", "Grand total", grandTotal);
        } catch (BookingException e) {
            System.out.println("Unexpected error: " + e.getMessage());
            logger.error("Unexpected error in valid bookings", e);
        }
    }

    private static void invalidBookings() {
        System.out.println();
        System.out.println("=== Invalid bookings (each one raises an exception) ===");
        LocalDate date = LocalDate.of(2026, 12, 1);

        // Luggage weight never entered
        FlightBooking noLuggage = new FlightBooking("F-002", "Ali Hassan", date, "Madinah", 300.0);
        try {
            computeTotalPrice(noLuggage);
        } catch (MissingInformationException e) {
            report(noLuggage, e);
        }

        // Luggage over 40 kg
        FlightBooking heavy = new FlightBooking("F-003", "Reem Fahad", date, "Cairo", 900.0);
        try {
            heavy.setLuggageWeight(55);
        } catch (InvalidArgumentException e) {
            report(heavy, e);
        }

        // Distance never entered
        TrainBooking noDistance = new TrainBooking("T-003", "Khalid Nasser", date, "Qassim", SeatClass.STANDARD);
        try {
            computeTotalPrice(noDistance);
        } catch (MissingInformationException e) {
            report(noDistance, e);
        }

        // Distance over 2000 km
        TrainBooking tooFar = new TrainBooking("T-004", "Huda Saad", date, "Tabuk", SeatClass.FIRST_CLASS);
        try {
            tooFar.setDistanceKm(2500);
        } catch (InvalidArgumentException e) {
            report(tooFar, e);
        }

        // Days never entered
        CarRentalBooking noDays = new CarRentalBooking("C-002", "Yousef Omar", date, "Taif", 150.0);
        try {
            computeTotalPrice(noDays);
        } catch (MissingInformationException e) {
            report(noDays, e);
        }

        // Zero rental days
        CarRentalBooking zeroDays = new CarRentalBooking("C-003", "Lama Adel", date, "Riyadh", 200.0);
        try {
            zeroDays.setDays(0);
        } catch (InvalidArgumentException e) {
            report(zeroDays, e);
        }
    }

    private static void report(Booking booking, BookingException e) {
        System.out.printf("%s -> %s: %s%n", booking.getBookingId(), e.getClass().getSimpleName(), e.getMessage());
        logger.error("Booking {} failed: {}", booking.getBookingId(), e.getMessage(), e);
    }
}
