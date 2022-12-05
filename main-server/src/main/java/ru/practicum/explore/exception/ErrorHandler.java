package ru.practicum.explore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.model.api.ApiError;

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
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                Timestamp.from(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserConflictException(final ObjectParameterConflictException e) {
        return new ApiError(e.getStackTrace(),
                HttpStatus.CONFLICT.name(),
                e.getMessage(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                Timestamp.from(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidParameterException(final InvalidParameterException e) {
        return new ApiError(e.getStackTrace(),
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Timestamp.from(Instant.now()));
    }
}