package ru.practicum.explore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.compilation.CompilationDto;
import ru.practicum.explore.model.compilation.NewCompilationDto;
import ru.practicum.explore.service.admin.CompilationAdminService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationAdminService compilationService;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compilationId}")
    public void deleteCompilationById(@PathVariable Long compilationId) {
        compilationService.deleteCompilationById(compilationId);
    }

    @PatchMapping("/{compilationId}/events/{eventId}")
    public CompilationDto addEventToCompilation(@PathVariable Long compilationId,
                                                @PathVariable Long eventId) {
        return compilationService.addEventToCompilation(compilationId, eventId);
    }

    @DeleteMapping("/{compilationId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compilationId,
                                           @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/{compilationId}/pin")
    public void pinCompilation(@PathVariable Long compilationId) {
        compilationService.pinCompilation(compilationId);
    }

    @DeleteMapping("/{compilationId}/pin")
    public void unpinCompilation(@PathVariable Long compilationId) {
        compilationService.unpinCompilation(compilationId);
    }
}