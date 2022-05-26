package com.example.booking.reservation.exception;

public class NoAvailableRoomsException extends Exception {

    public static final String EXCEPTION_MESSAGE = "There is no available rooms at that moment. " +
            "Please try again later or contact hotel support.";

    public NoAvailableRoomsException() {
        super(EXCEPTION_MESSAGE);
    }
}
