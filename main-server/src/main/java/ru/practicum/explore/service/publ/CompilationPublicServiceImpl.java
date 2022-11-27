package ru.practicum.explore.service.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.model.compilation.CompilationDto;
import ru.practicum.explore.repository.CompilationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService{
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto findCompilationById(Long compilationId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compilationId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Compilation with id %d not found", compilationId))));
    }

    @Override
    public List<CompilationDto> findAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            return CompilationMapper.toCompilationDtos(compilationRepository.findAllByPinned(pinned, pageable));
        } else {
            return CompilationMapper.toCompilationDtos(compilationRepository.findAll());
        }
    }
}