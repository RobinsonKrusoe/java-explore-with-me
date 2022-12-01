package ru.practicum.explore.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.explore.ewm.dto.CompilationDto;
import ru.practicum.explore.ewm.mapper.CompilationMapper;
import ru.practicum.explore.ewm.model.Compilation;
import ru.practicum.explore.ewm.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository repository;

    public PublicCompilationServiceImpl(CompilationRepository repository) {
        this.repository = repository;
    }

    /**
     * Получение подборок событий
     *
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return
     */
    @Override
    public Collection<CompilationDto> search(Boolean pinned, Integer from, Integer size) {
        Pageable pagingSet = PageRequest.of(from / size, size);

        Page<Compilation> retPage = repository.findByPinned(pinned, pagingSet);

        List<CompilationDto> ret = new ArrayList<>();
        for (Compilation compilation : retPage) {
            ret.add(CompilationMapper.toCompilationDto(compilation));
        }

        return ret;
    }

    /**
     * Получение подборки событий по его id
     *
     * @param compId
     * @return
     */
    @Override
    public CompilationDto get(Long compId) {
        return CompilationMapper.toCompilationDto(repository.getReferenceById(compId));
    }
}
