package ru.practicum.explore.controller.publ;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.compilation.CompilationDto;
import ru.practicum.explore.service.publ.CompilationPublicService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationPublicService compilationService;

    @GetMapping("/{compilationId}")
    public CompilationDto findCompilationByCompilationId(@PathVariable Long compilationId) {
        return compilationService.findCompilationById(compilationId);
    }

    @GetMapping
    public List<CompilationDto> findAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        return compilationService.findAllCompilations(pinned, from, size);
    }
}