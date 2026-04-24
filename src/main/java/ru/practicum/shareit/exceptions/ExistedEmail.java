package ru.practicum.shareit.exceptions;

public class ExistedEmail extends RuntimeException {
    public ExistedEmail(String message) {
        super(message);
    }
}
