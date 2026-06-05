package ru.practicum.shareit.exceptions;

public class NotFoundUserForBookingsException extends RuntimeException {
    public NotFoundUserForBookingsException(String message) {
        super(message);
    }
}
