package edu.spu.se411.lab07.exceptions;

/** Common parent of every booking error, so callers can catch them all at once. */
public class BookingException extends Exception {

    private static final long serialVersionUID = 1L;

    public BookingException(String message) {
        super(message);
    }
}
