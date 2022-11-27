package ru.practicum.explore.service.publ;

import ru.practicum.explore.model.compilation.CompilationDto;

import java.util.List;

public interface CompilationPublicService {

    CompilationDto findCompilationById(Long compilationId);

    List<CompilationDto> findAllCompilations(Boolean pinned, Integer from, Integer size);

}