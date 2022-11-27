package ru.practicum.statistic.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.statistic.model.EndpointHit;
import ru.practicum.statistic.model.EndpointHitDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .ip(endpointHitDto.getIp())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .build();
    }
}
