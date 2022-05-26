package com.example.booking.room.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long id) {
        super(String.format("Room with id: %d wasn't found", id));
    }
}
