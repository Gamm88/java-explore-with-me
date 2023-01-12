package ru.practicum.exeptions;

public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super(message);
    }
}