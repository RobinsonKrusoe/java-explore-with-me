package ru.practicum.explore.ewm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.CompilationDto;
import ru.practicum.explore.ewm.dto.NewCompilationDto;
import ru.practicum.explore.ewm.mapper.CompilationMapper;
import ru.practicum.explore.ewm.model.Compilation;
import ru.practicum.explore.ewm.model.Event;
import ru.practicum.explore.ewm.repository.CompilationRepository;
import ru.practicum.explore.ewm.repository.EventRepository;

import java.util.Set;

@Service
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    public AdminCompilationsServiceImpl(CompilationRepository repository,
                                        EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    /**
     * Добавление новой подборки
     *
     * @param newCompilationDto
     * @return
     */
    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null) {
            throw new ValidationException("Пустое название подборки!");
        }

        Compilation compDB = CompilationMapper.toCompilation(newCompilationDto);

        Set<Event> events = eventRepository.findByIdIn(newCompilationDto.getEvents());

        compDB.setEvents(events);

        repository.save(compDB);

        return CompilationMapper.toCompilationDto(compDB);
    }

    /**
     * Удаление подборки
     *
     * @param compId
     */
    @Override
    @Transactional
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
    @Transactional
    public void delEvent(Long compId, Long eventId) {
        Compilation compDB = repository.getReferenceById(compId);
        Event eventDB = getEvent(eventId);

        compDB.getEvents().remove(eventDB);

        repository.save(compDB);
    }

    /**
     * Добавить событие в подборку
     *
     * @param compId
     * @param eventId
     */
    @Override
    @Transactional
    public void addEvent(Long compId, Long eventId) {
        Compilation compDB = repository.getReferenceById(compId);
        Event eventDB = getEvent(eventId);

        compDB.getEvents().add(eventDB);

        repository.save(compDB);
    }

    /**
     * Открепить подборку на главной странице
     *
     * @param compId
     */
    @Override
    @Transactional
    public void unpin(Long compId) {
        Compilation compDB = repository.getReferenceById(compId);
        compDB.setPinned(false);
        repository.save(compDB);
    }

    /**
     * Закрепить подборку на главной странице
     *
     * @param compId
     */
    @Override
    @Transactional
    public void pin(Long compId) {
        Compilation compDB = repository.getReferenceById(compId);
        compDB.setPinned(true);
        repository.save(compDB);
    }

    /**
     * Получени события
     *
     * @param eventId
     * @return
     */
    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие #" + eventId + " не существует!"));
    }

}
