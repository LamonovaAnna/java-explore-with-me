package ru.practicum.explore.service.admin;

import ru.practicum.explore.model.compilation.CompilationDto;
import ru.practicum.explore.model.compilation.NewCompilationDto;

public interface CompilationAdminService {

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilationById(Long compilationId);

    CompilationDto addEventToCompilation(Long compilationId, Long eventId);

    void deleteEventFromCompilation(Long compilationId, Long eventId);

    void pinCompilation(Long compilationId);

    void unpinCompilation(Long compilationId);

}