package ru.practicum.explore.model.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Location {
    private Double lat;
    private Double lon;
}
