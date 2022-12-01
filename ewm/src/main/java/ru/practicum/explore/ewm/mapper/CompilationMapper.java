package ru.practicum.explore.ewm.mapper;

import ru.practicum.explore.ewm.dto.CompilationDto;
import ru.practicum.explore.ewm.dto.NewCompilationDto;
import ru.practicum.explore.ewm.model.Compilation;
import ru.practicum.explore.ewm.model.Event;

import java.util.ArrayList;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        if (compilation != null) {
            CompilationDto ret = CompilationDto.builder()
                    .id(compilation.getId())
                    .events(new ArrayList<>())
                    .pinned(compilation.getPinned())
                    .title(compilation.getTitle())
                    .build();

            if (compilation.getEvents() != null) {
                for (Event event: compilation.getEvents()) {
                    if (ret.getEvents() == null) {
                        ret.setEvents(new ArrayList<>());
                    }
                    ret.getEvents().add(EventMapper.toEventShortDto(event));
                }
            }
            return ret;
        } else {
            return null;
        }
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto != null) {
            Compilation compilation = new Compilation();
            compilation.setPinned(newCompilationDto.getPinned());
            compilation.setTitle(newCompilationDto.getTitle());
            return compilation;
        } else {
            return null;
        }
    }

    public static Compilation toCompilation(CompilationDto compilationDto) {
        if (compilationDto != null) {
            Compilation compilation = new Compilation();
            compilation.setId(compilationDto.getId());
            compilation.setPinned(compilationDto.getPinned());
            compilation.setTitle(compilationDto.getTitle());
            return compilation;
        } else {
            return null;
        }
    }
}
