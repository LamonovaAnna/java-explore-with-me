package ru.practicum.statistic.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistic.model.EndpointHitDto;
import ru.practicum.statistic.model.ViewStats;
import ru.practicum.statistic.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    public void createHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statService.createHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getAllHits(@RequestParam @NonNull LocalDateTime start,
                                      @RequestParam @NonNull LocalDateTime end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statService.getAllHits(start, end, uris, unique);
    }
}