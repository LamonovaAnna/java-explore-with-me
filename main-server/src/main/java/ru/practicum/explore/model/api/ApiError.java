package ru.practicum.explore.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ApiError {
    private StackTraceElement[] errors;
    private String message;
    private String reason;
    private String status;
    private Timestamp timestamp;

}
