package ru.practicum.explore.ewm.service;

import org.springframework.stereotype.Component;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.CompilationDto;
import ru.practicum.explore.ewm.dto.NewCompilationDto;
import ru.practicum.explore.ewm.mapper.CompilationMapper;
import ru.practicum.explore.ewm.model.Compilation;
import ru.practicum.explore.ewm.model.Event;
import ru.practicum.explore.ewm.repository.CompilationRepository;

import java.util.ArrayList;

@Component
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final CompilationRepository repository;
    private final AdminEventService eventService;

    public AdminCompilationsServiceImpl(CompilationRepository repository, AdminEventService eventService) {
        this.repository = repository;
        this.eventService = eventService;
    }

    /**
     * Добавление новой подборки
     *
     * @param newCompilationDto
     * @return
     */
    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null) {
            throw new ValidationException("Пустое название подборки!");
        }

        Compilation compDB = CompilationMapper.toCompilation(newCompilationDto);

        for (Long eventId : newCompilationDto.getEvents()) {
            if (compDB.getEvents() == null) {
                compDB.setEvents(new ArrayList<>());
            }
            compDB.getEvents().add(eventService.get(eventId));
        }

        compDB = repository.saveAndFlush(compDB);

        return CompilationMapper.toCompilationDto(compDB);
    }

    /**
     * Удаление подборки
     *
     * @param compId
     */
    @Override
    public void del(Long compId) {
        repository.deleteById(compId);
    }

    /**
     * Удалить событие из подборки
     *
     * @param compId
     * @param eventId
     */
    @Override
    public void delEvent(Long compId, Long eventId) {
        Compilation compDB = repository.getReferenceById(compId);
        Event eventDB = eventService.get(eventId);

        compDB.getEvents().remove(eventDB);

        compDB = repository.saveAndFlush(compDB);
    }

    /**
     * Добавить событие в подборку
     *
     * @param compId
     * @param eventId
     */
    @Override
    public void addEvent(Long compId, Long eventId) {
        Compilation compDB = repository.getReferenceById(compId);
        Event eventDB = eventService.get(eventId);

        if (compDB.getEvents() == null) {
            compDB.setEvents(new ArrayList<>());
        }

        compDB.getEvents().add(eventDB);

        compDB = repository.saveAndFlush(compDB);
    }

    /**
     * Открепить подборку на главной странице
     *
     * @param compId
     */
    @Override
    public void unpin(Long compId) {
        Compilation compDB = repository.getReferenceById(compId);
        compDB.setPinned(false);
        compDB = repository.saveAndFlush(compDB);
    }

    /**
     * Закрепить подборку на главной странице
     *
     * @param compId
     */
    @Override
    public void pin(Long compId) {
        Compilation compDB = repository.getReferenceById(compId);
        compDB.setPinned(true);
        compDB = repository.saveAndFlush(compDB);
    }
}
