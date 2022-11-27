package ru.practicum.explore.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.compilation.CompilationDto;
import ru.practicum.explore.model.compilation.NewCompilationDto;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.repository.CompilationRepository;
import ru.practicum.explore.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminServiceImpl implements CompilationAdminService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        return CompilationMapper.toCompilationDto(compilationRepository.save(
                CompilationMapper.toCompilationFromNew(newCompilationDto)));
    }

    @Override
    public void deleteCompilationById(Long compilationId) {
        checkCompilationExist(compilationId);
        log.info("Compilation with id {} was deleted", compilationId);
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto addEventToCompilation(Long compilationId, Long eventId) {
        checkCompilationExist(compilationId);
        checkEventExist(eventId);
        Compilation compilation = compilationRepository.getReferenceById(compilationId);
        Event event = eventRepository.getReferenceById(eventId);
        compilation.getEvents().add(event);

        log.info("Event with id {} was added to compilation with id {}", eventId, compilationId);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteEventFromCompilation(Long compilationId, Long eventId) {
        checkCompilationExist(compilationId);
        checkEventExist(eventId);

        Compilation compilation = compilationRepository.getReferenceById(compilationId);
        List<Event> events = compilation.getEvents();
        events.removeIf(e -> e.getId().equals(eventId));

        if (compilation.getEvents().size() != events.size()) {
            compilation.setEvents(events);
            log.info("Event with id {} was deleted from compilation with id {}", eventId, compilationId);
            compilationRepository.save(compilation);
        }
    }

    @Override
    public void pinCompilation(Long compilationId) {
        checkCompilationExist(compilationId);
        Compilation compilation = compilationRepository.getReferenceById(compilationId);
        compilation.setPinned(true);

        log.info("Compilation with id {} was pinned", compilationId);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compilationId) {
        checkCompilationExist(compilationId);
        Compilation compilation = compilationRepository.getReferenceById(compilationId);
        compilation.setPinned(false);

        log.info("Compilation with id {} was unpinned", compilationId);
        compilationRepository.save(compilation);
    }

    private void checkCompilationExist(Long compilationId) {
        if (!compilationRepository.existsById(compilationId)) {
            log.info("Compilation with id {} wasn't found", compilationId);
            throw new ObjectNotFoundException(String.format("Compilation with id %d wasn't found", compilationId));
        }
    }

    private void checkEventExist(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.info("Event with id {} wasn't found", eventId);
            throw new ObjectNotFoundException(String.format("Event with id %d wasn't found", eventId));
        }
    }
}