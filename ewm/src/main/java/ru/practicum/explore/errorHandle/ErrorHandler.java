package ru.practicum.explore.errorHandle;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.errorHandle.exception.EntityAlreadyExistException;
import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.errorHandle.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Обработчик ошибок
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEntityAlreadyExistException(final EntityAlreadyExistException e) {
        log.error("Ошибка дублирования {}", e.getMessage());
        return new ErrorResponse(List.of(),
                e.getMessage(),
                "Ошибка дублирования",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.error("Объект не найден {}", e.getMessage());
        return new ErrorResponse(List.of(),
                e.getMessage(),
                "Объект не найден",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("Ошибка валидации {}", e.getMessage());
        return new ErrorResponse(List.of(),
                e.getMessage(),
                "Ошибка валидации",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final MethodArgumentNotValidException e) {
        log.error("Ошибка валидации аргумента {}", e.getMessage());
        return new ErrorResponse(List.of(),
                e.getMessage(),
                "Ошибка валидации аргумента",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final MissingServletRequestParameterException e) {
        log.error("Отсутствует аргумент {}, {}", e.getParameterName(), e.getMessage());

        return new ErrorResponse(List.of(),
                e.getMessage(),
                "Отсутствует аргумент " + e.getParameterName(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJsonProcessingException(final JsonProcessingException e) {
        log.error("Отсутствует аргумент {}", e.getMessage());

        return new ErrorResponse(List.of(),
                e.getMessage(),
                "Ошибка формирования сущности",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse(List.of(),
                e.getMessage(),
                "Произошла непредвиденная ошибка! " + e.getStackTrace(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now());
    }
}
