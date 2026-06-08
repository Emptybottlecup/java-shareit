package ru.practicum.shareit.exceptions;

public class NotFoundBookingException extends RuntimeException {
    public NotFoundBookingException(String message) {
        super(message);
    }
}
