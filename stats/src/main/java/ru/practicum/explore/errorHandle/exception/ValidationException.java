package ru.practicum.explore.errorHandle.exception;

//непроверяемое исключение
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
