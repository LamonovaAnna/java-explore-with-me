package ru.practicum.explore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.Instant;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final ObjectNotFoundException e) {
        return new ApiError(e.getStackTrace(),
                HttpStatus.NOT_FOUND.name(),
                e.getMessage(),
                "Not found",
                Timestamp.from(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserConflictException(final ObjectParameterConflictException e) {
        return new ApiError(e.getStackTrace(),
                HttpStatus.CONFLICT.name(),
                e.getMessage(),
                "Conflict",
                Timestamp.from(Instant.now()));
    }
}